package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> getAll();
    Mpa getRatingBy(Long id);
    void getFilmMpa(Film film);

    void getFilmsMpa(List<Film> films);
}
