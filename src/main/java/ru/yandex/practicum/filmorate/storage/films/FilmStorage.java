package ru.yandex.practicum.filmorate.storage.films;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> findAll();
    Film create(Film film);
    Film update(Film film);
    Optional<Film> findFilmById(Long id);
    void like(Long id, Long userID);
    void removeLike(Long id, Long userID);
    List<Film> getTheBest(Integer count);
    void delete(Long id);
}

