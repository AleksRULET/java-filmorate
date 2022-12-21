package ru.yandex.practicum.filmorate.storage.films;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@Qualifier
public class DBFilmStorage implements FilmStorage {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public DBFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Film> getAll() {
        String sqlQuery = "SELECT FILM_ID, NAME, RELEASE_DATE, DESCRIPTION, DURATION, RATE FROM FILMS";
        List<Film> f = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
        for (Film film: f) {
            getFilmGenre(film);
            getFilmMpa(film);
        }
        return f;
    }

    public Film create(Film film) {
        String sqlQuery = "INSERT INTO FILMS (NAME, RELEASE_DATE, DESCRIPTION, DURATION, RATE, RATING) VALUES(?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(2, Types.DATE);
            } else {
                stmt.setDate(2, Date.valueOf(releaseDate));
            }
            stmt.setString(3, film.getDescription());
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setLong(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        setFilmGenre(film);
        return film;
    }

    public Film update(Film film) {
        String sqlQuery = "UPDATE FILMS SET NAME = ?, RELEASE_DATE = ?, DESCRIPTION = ?, DURATION = ?, RATE = ?, RATING = ? WHERE FILM_ID = ?";
        int i = jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getReleaseDate()
                , film.getDescription()
                , film.getDuration()
                , film.getRate()
                , film.getMpa().getId()
                , film.getId());
        setFilmGenre(film);
        if (i != 1) {
            throw new ObjectNotFoundException("Фильм не найден");
        }
        return film;
    }

    public Film getFilmByID(Long id) {
        final String sqlQuery = "SELECT FILM_ID, FILMS.NAME, RELEASE_DATE, " +
                "DESCRIPTION, DURATION, RATE, RATING_ID, RATING_NAME " +
                "FROM FILMS JOIN RATING_MPA WHERE FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), id);
        if (films.isEmpty()) {
            throw new ObjectNotFoundException("Фильм не найден");
        }
        getFilmGenre(films.get(0));
        getFilmMpa(films.get(0));
        return films.get(0);
    }

    public void like(Long id, Long userID) {
        String sqlQuery = "INSERT INTO FAVORITE_FILMS (FILM_ID, USER_ID) VALUES(?,?)";
        int i = jdbcTemplate.update(sqlQuery
                , id
                , userID);
        if (i != 1) {
            throw new ObjectNotFoundException("Фильм не найден");
        }
    }

    public void removeLike(Long id, Long userID) {
        String sqlQuery = "DELETE FROM FAVORITE_FILMS WHERE FILM_ID = ? AND USER_ID = ?";
        if (jdbcTemplate.update(sqlQuery, id, userID) == 0) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    public List<Film> getTheBest(Integer count) {
        String sqlQuery = "SELECT FILMS.FILM_ID, FILMS.NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, M.RATING_ID," +
                "M.RATING_NAME FROM FILMS " +
                "LEFT JOIN FAVORITE_FILMS FF ON FILMS.FILM_ID = FF.FILM_ID " +
                "JOIN RATING_MPA M ON FILMs.RATING = M.RATING_ID " +
                "GROUP BY FILMS.FILM_ID, FF.FILM_ID IN (SELECT FILM_ID FROM FAVORITE_FILMS) " +
                "ORDER BY COUNT(FF.FILM_ID) DESC LIMIT ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), count);
        for (Film f: films) {
            getFilmGenre(f);
            getFilmMpa(f);
        }
        return films;
    }

    public void delete(Long id) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    void setFilmGenre(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_GENRE FILM_ID WHERE FILM_ID = ?", film.getId());
        if (film.getGenres() != null) {
            String sql;
            Set<Long> checkMAP = new HashSet<>();
            Set<Genre> newSet = new HashSet<>();
            for (Genre genre : film.getGenres()) {
                if (!newSet.contains(genre) && !checkMAP.contains(genre.getId())) {
                    sql = String.valueOf(String.format("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES(%d, %d);", film.getId(), genre.getId()));
                    jdbcTemplate.execute(sql);
                    checkMAP.add(genre.getId());
                    newSet.add(genre);
                }
            }
            film.setGenres(newSet);

        }
    }
    public void getFilmGenre(Film film) {
        SqlRowSet row = jdbcTemplate.queryForRowSet("SELECT FG.GENRE_ID, G.NAME FROM FILM_GENRE FG " +
                "JOIN GENRES G ON FG.GENRE_ID = G.GENRE_ID WHERE FG.FILM_ID = ?;", film.getId());
        while (row.next()) {
            Genre genre = new Genre(row.getLong("GENRE_ID"), row.getString("NAME"));
            film.addGenre(genre);
        }
    }

    public void getFilmMpa(Film film) {
        SqlRowSet row = jdbcTemplate.queryForRowSet("SELECT RM.RATING_ID, RM.RATING_NAME FROM FILMS F " +
                "JOIN RATING_MPA RM ON F.RATING = RM.RATING_ID WHERE FILM_ID = ?", film.getId());
        if (row.next()) {
            film.setMpa(new Mpa(row.getLong("RATING_ID"), row.getString("RATING_NAME")));
        }
    }

    static Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film(rs.getLong("FILM_ID"),
                rs.getString("NAME"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getString("DESCRIPTION"), rs.getInt("DURATION"), rs.getInt("RATE"));
        if (film.getGenres() == null) {
            film.setGenres(new LinkedHashSet<>());
        }
    return film;
    }

}
