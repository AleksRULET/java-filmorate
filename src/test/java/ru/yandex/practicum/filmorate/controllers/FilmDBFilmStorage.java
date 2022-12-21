package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.films.DBFilmStorage;
import ru.yandex.practicum.filmorate.storage.users.DBUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FilmDBFilmStorage extends TestData {

    public final DBFilmStorage dbFilmStorage;
    public final DBUserStorage dbUserStorage;

    @BeforeEach
    public void createTestData() {
        user1 = dbUserStorage.create(user1);
        user1 = dbUserStorage.create(user1);
        user3 = dbUserStorage.create(user3);
        new ArrayList<>(List.of(film1, film2))
                .forEach(dbFilmStorage::create);
    }

    @Test
    public void testGetFilm() {
        film3.setId(dbFilmStorage.update(film3).getId());

    }

    @Test
    public void testGetFilms() {
        assertFalse(dbFilmStorage.getAll().contains(film1));
        assertFalse(dbFilmStorage.getAll().contains(film2));
    }

    @Test
    public void testAddFilm() {
    }

    @Test
    public void testUpdateFilm() {
        film2.setName("test name");
        film2.setDescription("test description");
        film2.setRate(2);
        film2.setMpa(new Mpa(5L,"NC-17"));
        film2.setGenres((new HashSet<>(List.of(
                new Genre(6L,"Боевик")
        ))));
        film2.setId(dbFilmStorage.update(film2).getId());
        assertEquals(film2.toString(), "Film(id=8, name=test name, releaseDate=2000-01-01, description=test description, duration=1, mpa=Mpa(id=&d, name=&s), rate=2, genres=[ru.yandex.practicum.filmorate.model.Genre@96a7e239])");
    }

    @Test
    public void testPopularMoviesLimit2() {
        dbFilmStorage.getAll().forEach(e ->
        {
            e.setRate(0);
            dbFilmStorage.update(e);
        });
        film1.setRate(2);
        film2.setRate(3);
        dbFilmStorage.update(film1);
        dbFilmStorage.update(film2);


                dbFilmStorage.getTheBest(2);
    }

    @Test
    public void testPopularMoviesLimit1() {
        dbFilmStorage.getAll().forEach(e ->
        {
            e.setRate(0);
            dbFilmStorage.update(e);
        });
        film1.setRate(1);
        film2.setRate(3);
        dbFilmStorage.update(film1);
        dbFilmStorage.update(film2);


                dbFilmStorage.getTheBest(1);
    }

    @Test
    public void testAddLike() {
        film1.setRate(1);
        dbFilmStorage.update(film1);

        dbFilmStorage.like(film1.getId(), user3.getId());
        film1.setRate(2);
        assertEquals(1,
                dbFilmStorage.getFilmByID(film1.getId()).getRate());
    }
}

