package ru.yandex.practicum.filmorate.controllers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@Slf4j
@Data
public class FilmController {
    private int id = 1;
    private HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public Collection<Film> getAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        if (!(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))) {
            film.setId(id);
            id++;
            log.debug("Добавлен фильм {}", film);
            films.put(film.getId(), film);
            return film;
        }
        throw new ValidationException("Дата некорректна");
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            if (!(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))) {
                films.put(film.getId(), film);
                log.debug("Обновлён фильм {}", film);
                return film;
            }
            throw new ValidationException("Дата некорректна");
        }
        throw new ValidationException("Такого фильма не существует");
    }
}
