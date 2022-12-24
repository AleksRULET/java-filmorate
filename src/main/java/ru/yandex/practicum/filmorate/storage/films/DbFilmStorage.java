package ru.yandex.practicum.filmorate.storage.films;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

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
public class DbFilmStorage implements FilmStorage {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Film> findAll() {
        String sqlQuery = "SELECT FILM_ID, NAME, RELEASE_DATE, DESCRIPTION, DURATION, RATE FROM FILMS";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
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
        if (i != 1) {
            throw new ObjectNotFoundException("Фильм не найден");
        }

        return film;
    }

    public Optional<Film> findFilmById(Long id) {
        final String sqlQuery = "SELECT FILM_ID, FILMS.NAME, RELEASE_DATE, " +
                "DESCRIPTION, DURATION, RATE, RATING_ID, RATING_NAME " +
                "FROM FILMS JOIN RATING_MPA WHERE FILM_ID = ?";
        try {
            final List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), id);
            return Optional.ofNullable(films.get(0));
        } catch (RuntimeException runtimeException) {
            return Optional.empty();
        }
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

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), count);
    }

    public void delete(Long id) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    static public Film makeFilm(ResultSet rs) throws SQLException {
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
