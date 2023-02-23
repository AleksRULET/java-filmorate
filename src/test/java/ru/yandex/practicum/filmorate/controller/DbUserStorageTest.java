package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.util.ArrayList;
import java.util.List;

@AutoConfigureTestDatabase
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DbUserStorageTest extends TestData {
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
}