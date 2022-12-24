package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FilmDbFilmStorage extends TestData {

    public final DbFilmStorage DbFilmStorage;
    public final DbUserStorage DbUserStorage;

    @BeforeEach
    public void createTestData() {
        user1 = DbUserStorage.create(user1);
        user1 = DbUserStorage.create(user1);
        user3 = DbUserStorage.create(user3);
        new ArrayList<>(List.of(film1, film2))
                .forEach(DbFilmStorage::create);
    }



    @Test
    public void testGetFilms() {
        assertFalse(DbFilmStorage.findAll().contains(film1));
        assertFalse(DbFilmStorage.findAll().contains(film2));
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
        DbFilmStorage.update(film2);
        film2.setId(8L);
        assertEquals(film2.toString(), "Film(id=8, name=test name, releaseDate=2000-01-01, description=test description, duration=1, mpa=Mpa(id=&d, name=&s), rate=2, genres=[ru.yandex.practicum.filmorate.model.Genre@96a7e239])");
    }

    @Test
    public void testPopularMoviesLimit2() {
        DbFilmStorage.findAll().forEach(e ->
        {
            e.setRate(0);
            DbFilmStorage.update(e);
        });
        film1.setRate(2);
        film2.setRate(3);
        DbFilmStorage.update(film1);
        DbFilmStorage.update(film2);


                DbFilmStorage.getTheBest(2);
    }

    @Test
    public void testPopularMoviesLimit1() {
        DbFilmStorage.findAll().forEach(e ->
        {
            e.setRate(0);
            DbFilmStorage.update(e);
        });
        film1.setRate(1);
        film2.setRate(3);
        DbFilmStorage.update(film1);
        DbFilmStorage.update(film2);


                DbFilmStorage.getTheBest(1);
    }

    @Test
    public void testAddLike() {
        film1.setRate(1);
        DbFilmStorage.update(film1);

        DbFilmStorage.like(film1.getId(), user3.getId());
        film1.setRate(2);
        assertEquals(1,
                DbFilmStorage.findFilmById(film1.getId()).get().getRate());
    }
}

