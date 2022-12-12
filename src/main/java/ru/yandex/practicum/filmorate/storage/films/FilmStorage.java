package ru.yandex.practicum.filmorate.storage.films;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();
    Film create(Film film);
    Film update(Film film);
    Film getFilmByID(Long id);
    void like(Long id, Long userID);
    void removeLike(Long id, Long userID);
    List<Film> getTheBest(Integer count);
}

