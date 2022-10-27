package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private int id = 1;
    private HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
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

    @PutMapping
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
