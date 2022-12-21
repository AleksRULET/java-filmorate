package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Qualifier
public class DBMpaStorage implements MpaStorage {
    JdbcTemplate jdbcTemplate;

    public DBMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
}
