package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.films.DBFilmStorage;
import ru.yandex.practicum.filmorate.storage.users.DBUserStorage;

import java.util.ArrayList;
import java.util.List;

@AutoConfigureTestDatabase
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DBUserStorageTest extends TestData {
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
}