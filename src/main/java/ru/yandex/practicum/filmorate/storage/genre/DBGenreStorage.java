package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Qualifier
public class DBGenreStorage implements GenreStorage{
    JdbcTemplate jdbcTemplate;

    public DBGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Genre> getAll() {
        String sqlQuery = "SELECT GENRE_ID, NAME FROM GENRES";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs));
    }

    public Genre getGenreById(Long id) {
        String sqlQuery = "SELECT GENRE_ID, NAME FROM GENRES WHERE GENRE_ID = ?";
        List<Genre> g = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), id);
        if (g.isEmpty()) {
            throw new ObjectNotFoundException("Жанр не найден");
        }
        return g.get(0);
    }

    static Genre makeGenre (ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("GENRE_ID"), rs.getString("NAME"));
    }
}
