package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.management.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Qualifier
@RequiredArgsConstructor
public class DbMpaStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Mpa> getAll() {
        String sqlQuery = "SELECT RATING_ID, RATING_NAME FROM RATING_MPA";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpa(rs));
    }

    public Mpa getRatingBy(Long id) {
        String sqlQuery = "SELECT RATING_ID, RATING_NAME FROM RATING_MPA WHERE RATING_ID = ?";
        List<Mpa> m = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpa(rs), id);
        if (m.isEmpty()) {
            throw new ObjectNotFoundException("Рейтинг не найден");
        }
        return m.get(0);
    }

    static Mpa makeMpa (ResultSet rs) throws SQLException {
        return new Mpa(rs.getLong("RATING_ID"), rs.getString("RATING_NAME"));
    }

    public void getFilmMpa(Film film) {
        SqlRowSet row = jdbcTemplate.queryForRowSet("SELECT RM.RATING_ID, RM.RATING_NAME FROM FILMS F " +
                "JOIN RATING_MPA RM ON F.RATING = RM.RATING_ID WHERE FILM_ID = ?", film.getId());
        if (row.next()) {
            film.setMpa(new Mpa(row.getLong("RATING_ID"), row.getString("RATING_NAME")));
        }
    }

    public void getFilmsMpa(List<Film> films) {

        String sqlGenres = "SELECT FILM_ID, R.* " +
                "FROM FILMS " +
                "JOIN RATING_MPA R ON R.RATING_ID = FILMS.RATING " +
                "WHERE FILM_ID IN (:ids)";

        List<Long> ids = films.stream()
                .map(Film::getId)
                .collect(Collectors.toList());

        Map<Long, Film> filmMap = films.stream()
                .collect(Collectors.toMap(Film::getId, film -> film, (a, b) -> b));

        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);

        SqlRowSet sqlRowSet = namedParameterJdbcTemplate.queryForRowSet(sqlGenres, parameters);

        while (sqlRowSet.next()) {
            Long filmId = sqlRowSet.getLong("FILM_ID");
            Long mpaId = sqlRowSet.getLong("RATING_ID");
            String name = sqlRowSet.getString("RATING_NAME");

            filmMap.get(filmId).setMpa(new Mpa(mpaId, name));
        }
    }
}
