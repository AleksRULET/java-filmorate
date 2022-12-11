package ru.yandex.practicum.filmorate.storage.films;

import ru.yandex.practicum.filmorate.model.Film;


import java.util.ArrayList;

public interface FilmStorage {
    ArrayList<Film> getAll();
    Film create(Film film);
    Film update(Film film);
    Film getFilmByID(int id);
}

