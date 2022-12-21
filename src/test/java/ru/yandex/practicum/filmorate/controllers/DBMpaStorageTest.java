package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.mpa.DBMpaStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DBMpaStorageTest {

    public final DBMpaStorage mpaRepository;

    @Test
    public void testGetMpa() {
        assertEquals("NC-17", mpaRepository.getRatingBy(5L).getName());
    }

    @Test
    public void testGetMpas() {
        assertEquals(5, mpaRepository.getAll().size());
    }
}