package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.genre.DbGenreStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DbGenreStorageTests {

    public final DbGenreStorage DbGenreStorage;

    @Test
    public void testGetMpa() {
        assertEquals("Триллер", DbGenreStorage.findGenreById(4L).get().getName());
    }

    @Test
    public void testGetMpas() {
        assertEquals(6, DbGenreStorage.findAll().size());
    }
}
