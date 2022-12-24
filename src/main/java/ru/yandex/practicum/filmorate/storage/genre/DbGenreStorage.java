package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


@Component
@Qualifier
@RequiredArgsConstructor
public class DbGenreStorage implements GenreStorage{
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Genre> findAll() {
        String sqlQuery = "SELECT GENRE_ID, NAME FROM GENRES";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs));
    }

    public Optional<Genre> findGenreById(Long id) {
        String sqlQuery = "SELECT GENRE_ID, NAME FROM GENRES WHERE GENRE_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), id);
        if (genres.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(genres.get(0));
    }

    static Genre makeGenre (ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("GENRE_ID"), rs.getString("NAME"));
    }

    public void getFilmGenre(Film film) {
        SqlRowSet row = jdbcTemplate.queryForRowSet("SELECT FG.GENRE_ID, G.NAME FROM FILM_GENRE FG " +
                "JOIN GENRES G ON FG.GENRE_ID = G.GENRE_ID WHERE FG.FILM_ID = ?;", film.getId());
        while (row.next()) {
            Genre genre = new Genre(row.getLong("GENRE_ID"), row.getString("NAME"));
            film.addGenre(genre);
        }
    }

    public void getFilmsGenres(List<Film> films) {

        String sql = "SELECT FILM_ID, G.* " +
                "FROM FILM_GENRE " +
                "JOIN GENRES G ON G.GENRE_ID = FILM_GENRE.GENRE_ID " +
                "WHERE FILM_ID IN (:ids)";

        List<Long> ids = films.stream()
                .map(Film::getId)
                .collect(Collectors.toList());

        Map<Long, Film> filmMap = films.stream()
                .collect(Collectors.toMap(Film::getId, film -> film, (a, b) -> b));

        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);

        SqlRowSet sqlRowSet = namedParameterJdbcTemplate.queryForRowSet(sql, parameters);

        while (sqlRowSet.next()) {
            Long filmId = sqlRowSet.getLong("FILM_ID");
            Long genreId = sqlRowSet.getLong("GENRE_ID");
            String name = sqlRowSet.getString("NAME");

            filmMap.get(filmId).getGenres().add(new Genre(genreId, name));
        }
    }

    public void setFilmGenre(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID = ?", film.getId());
        if (film.getGenres() != null) {
            String sql;
            Set<Long> checkMAP = new HashSet<>();
            Set<Genre> newSet = new HashSet<>();
            for (Genre genre : film.getGenres()) {
                if (!newSet.contains(genre) && !checkMAP.contains(genre.getId())) {
                    sql = String.valueOf(String.format("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) " +
                            "VALUES(%d, %d);", film.getId(), genre.getId()));
                    jdbcTemplate.execute(sql);
                    checkMAP.add(genre.getId());
                    newSet.add(genre);
                }
            }
            film.setGenres(newSet);
        }
    }
}
