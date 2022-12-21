package ru.yandex.practicum.filmorate.controllers;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;


public class TestData {

    Film film1 = new Film(1L, "Titanic", LocalDate.of(1999, 12, 6),
            "Description", 189, new Mpa(2L,"R"), 1, new HashSet<>(List.of(
            new Genre(2L, "Документальный"),
            new Genre(6L, "Драма"))));

    Film film2 = new Film(1L, "Titanic", LocalDate.of(2000,1,1),
            "Desc",1,  new Mpa(1L,"R"), 1, new HashSet<>(List.of(
            new Genre(2L, "Драма"),
            new Genre(6L, "Боевик"))));


    Film film3 = new Film(1L, "Avatar", LocalDate.of(2009,1,1),
            "Desc",1,  new Mpa(1L,"R"), 1, new HashSet<>(List.of(
            new Genre(2L, "Драма"),
            new Genre(6L, "Боевик"))));

    User user1 = new User(1L,"tom","tom", "tom@mail.ru", LocalDate.of(2000,03, 13));
    User user3 = new User(3L,"krisi","krisi", "krisi@mail.ru", LocalDate.of(1985, 01, 10));


}