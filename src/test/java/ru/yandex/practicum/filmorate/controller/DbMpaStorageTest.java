package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.mpa.DbMpaStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DbMpaStorageTest {

    public final DbMpaStorage mpaRepository;

    @Test
    public void testGetMpa() {
        assertEquals("NC-17", mpaRepository.findRatingById(5L).get().getName());
    }

    @Test
    public void testGetMpas() {
        assertEquals(5, mpaRepository.findAll().size());
    }
}