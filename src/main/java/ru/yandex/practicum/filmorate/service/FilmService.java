package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public List<Film> getAll() {
        List<Film> films = filmStorage.findAll();
        genreStorage.getFilmsGenres(films);
        mpaStorage.getFilmsMpa(films);

        return films;
    }

    public Film create(Film film) {
        Film f = filmStorage.create(film);
        genreStorage.setFilmGenre(film);

        return f;
    }

    public Film update(Film film) {
        if(filmStorage.update(film) == 0) {
            throw new ObjectNotFoundException("Фильм не найден");
        }
        genreStorage.setFilmGenre(film);

        return film;
    }

    public Film getFilmById(Long id) {
        Film film = filmStorage.findFilmById(id).orElseThrow(() ->new ObjectNotFoundException("Фильм не найден"));
        genreStorage.getFilmGenre(film);
        mpaStorage.getFilmMpa(film);

        return film;
    }

    public void like(Long id, Long userID) {
        if (filmStorage.like(id, userID) == 0) throw  new ObjectNotFoundException("Пользователь с id=" + id + " или с id=" + userID + " не найден");
    }

    public void removeLike(Long id, Long userID) {
        if (filmStorage.removeLike(id, userID) == 0) throw  new ObjectNotFoundException("Лайк не найден");
    }

    public List<Film> getTheBest(Integer count) {
        List<Film> films = filmStorage.getTheBest(count);

        mpaStorage.getFilmsMpa(films);
        genreStorage.getFilmsGenres(films);
        return films;
    }

    public void delete(Long id){
        filmStorage.delete(id);
    }
}
