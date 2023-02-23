package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {
    List<Film> findAll();
    Film create(Film film);
    int update(Film film);
    Optional<Film> findFilmById(Long id);
    int like(Long id, Long userID);
    int removeLike(Long id, Long userID);
    List<Film> getTheBest(Integer count);
    void delete(Long id);
}

