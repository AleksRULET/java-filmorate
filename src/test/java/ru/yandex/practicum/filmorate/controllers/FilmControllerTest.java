package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.films.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;
    @BeforeEach
    void init() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));}
    @Test
    void getAll() {
        Assertions.assertEquals(0, filmController.getAll().size());
        filmController.create(new Film(0L, "Film", LocalDate.of(2000,1,1), "Desc", 90, 5, Set.of(1L)));
        filmController.create(new Film(1L, "Film2", LocalDate.of(2000,1,1), "Desc", 91, 5, Set.of(1L)));
        Collection<Film> films =  filmController.getAll();
        Assertions.assertEquals(2, films.size());
        Assertions.assertEquals(films.toArray()[1].toString(), "Film(id=2, name=Film2, releaseDate=2000-01-01, description=Desc, duration=91, rate=5, likes=[])");
    }

    @Test
    void create() {
        Film film1 = new Film(0L, "Film", LocalDate.of(1895,12,28), "Desc", 90, 5, Set.of(1L));
        filmController.create(film1);
        Assertions.assertEquals(filmController.getAll().get(0).getName(), "Film");
        Assertions.assertEquals(filmController.getAll().get(0).getId(), 1);
        Assertions.assertEquals(filmController.getAll().get(0).getDescription(), "Desc");
        Assertions.assertEquals(filmController.getAll().get(0).getReleaseDate(), LocalDate.of(1895,12,28));
        Assertions.assertEquals(filmController.getAll().get(0).getDuration(), 90);

        film1.setReleaseDate(LocalDate.of(1895, 12, 27));


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> filmController.create(film1));
        assertEquals("???????? ??????????????????????", exception.getMessage(), "???????????? ?? ???????????????????????? ????????????????????");
    }

    @Test
    void update() {
        Film film1 = new Film(0L, "Film", LocalDate.of(1895,12,28), "Desc", 90, 1, Set.of(1L));
        filmController.create(film1);
        Film film2 = new Film(1L, "Film2", LocalDate.of(1896,1,1), "Desc", 95, 1, Set.of(1L));
        filmController.update(film2);
        Assertions.assertEquals(filmController.getAll().get(0).getName(), "Film2");
        Assertions.assertEquals(filmController.getAll().get(0).getId(), 1);
        Assertions.assertEquals(filmController.getAll().get(0).getDescription(), "Desc");
        Assertions.assertEquals(filmController.getAll().get(0).getReleaseDate(), LocalDate.of(1896,1,1));
        Assertions.assertEquals(filmController.getAll().get(0).getDuration(), 95);

        Film film3 = new Film(0L, "Film", LocalDate.of(1894,1,1), "Desc", 90, 5, Set.of(1L));
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> filmController.update(film3));
        assertEquals("???????? ??????????????????????", exception.getMessage(), "???????????? ?? ???????????????????????? ????????????????????");
        film3.setId(1L);
        RuntimeException exception2 = assertThrows(
                RuntimeException.class,
                () -> filmController.update(film3));
        assertEquals("???????? ??????????????????????", exception2.getMessage(), "???????????? ?? ???????????????????????? ????????????????????");
    }
}