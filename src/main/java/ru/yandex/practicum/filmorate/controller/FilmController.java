package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAll() {
        List<Film> films = filmService.getAll();
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new BadRequestException("Дата некорректна");
        }
        log.debug("Добавлен фильм {}", film);

        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new BadRequestException("Дата некорректна");
        }
        log.debug("Обновлён фильм {}", film);

        return filmService.update(film);
    }

    @GetMapping(value = "{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("{id}/like/{userid}")
    public void like(@PathVariable Long id, @PathVariable Long userid) {
        filmService.like(id, userid);
    }

    @DeleteMapping("{id}/like/{userid}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userid) {
        filmService.removeLike(id, userid);
    }

    @GetMapping(value = "popular")
    public List<Film> getTheBest(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTheBest(count);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        filmService.delete(id);
    }
}
