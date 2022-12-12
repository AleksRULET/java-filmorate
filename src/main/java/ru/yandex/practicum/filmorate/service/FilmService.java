package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getFilmByID(Long id) {
        return filmStorage.getFilmByID(id);
    }

    public void like(Long id, Long userID) {
        filmStorage.like(id, userID);
    }

    public void removeLike(Long id, Long userID) {
        filmStorage.removeLike(id, userID);
    }

    public List<Film> getTheBest(Integer count) {
        return filmStorage.getTheBest(count);
    }
}
