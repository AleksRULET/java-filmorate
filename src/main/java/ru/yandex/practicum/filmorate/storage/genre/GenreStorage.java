package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAll();
    Genre getGenreById(Long id);
    void getFilmGenre(Film film);

    void getFilmsGenres(List<Film> films);

    void setFilmGenre(Film film);
}
