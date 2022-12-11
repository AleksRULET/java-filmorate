package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;

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

    public Film getFilmByID(int id) {
        return filmStorage.getFilmByID(id);
    }

    public void like(int id, int userID) {
        filmStorage.getFilmByID(id).like(userID);
    }

    public void removeLike(int id, int userID) {
        filmStorage.getFilmByID(id).removeLike(userID);
    }

    public List<Film> getTheBest(Integer count) {
        if (count == null) {
            count = 10;
        }
        List<Film> sortedList = new ArrayList<>(filmStorage.getAll());
        Collections.reverse(sortedList);
        return sortedList.stream().limit(count).collect(Collectors.toList());
    }
}
