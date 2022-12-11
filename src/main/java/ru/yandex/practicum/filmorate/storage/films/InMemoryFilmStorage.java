package ru.yandex.practicum.filmorate.storage.films;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 1;
    private HashMap<Integer, Film> films = new HashMap<>();

    public ArrayList<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    public Film create(Film film) {
            film.setId(id);
            id++;
            film.setLikes(new HashSet<>());
            films.put(film.getId(), film);
            return film;
    }

    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            film.setLikes(new HashSet<>());
            films.put(film.getId(), film);
            return film;
        }
        throw new ObjectNotFoundException("Фильм не найден");
    }

    public Film getFilmByID(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new ObjectNotFoundException("Фильм не найден");
    }


}
