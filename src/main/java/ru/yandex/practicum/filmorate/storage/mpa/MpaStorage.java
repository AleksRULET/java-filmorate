package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    List<Mpa> findAll();
    Optional<Mpa> findRatingById(Long id);
    void getFilmMpa(Film film);

    void getFilmsMpa(List<Film> films);
}
