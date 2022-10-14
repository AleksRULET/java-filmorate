package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;
    @BeforeEach
    void init() {
        userController = new UserController();
    }
    @Test
    void getAll() {
        Assertions.assertEquals(0, userController.getAll().size());
        userController.create(new User("login", "User1",0, "email@yandex.ru", LocalDate.now()));
        userController.create(new User("login", "User2", 0, "email@yandex.ru", LocalDate.now()));
        Collection<User> users =  userController.getAll();
        Assertions.assertEquals(2, users.size());
        Assertions.assertEquals(users.toArray()[1].toString(), "User(login=login, name=User2, id=2, email=email@yandex.ru, birthday=2022-10-15)");
    }

    @Test
    void create() {
        User user1 = new User("login", "User1", 0, "email@yandex.ru", LocalDate.now());
        userController.create(user1);
        Assertions.assertEquals(userController.getUsers().get(1).getName(), "User1");
        Assertions.assertEquals(userController.getUsers().get(1).getId(), 1);
        Assertions.assertEquals(userController.getUsers().get(1).getLogin(), "login");
        Assertions.assertEquals(userController.getUsers().get(1).getBirthday(), LocalDate.now());
        Assertions.assertEquals(userController.getUsers().get(1).getEmail(), "email@yandex.ru");

        user1.setLogin("User 1");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userController.create(user1));
        assertEquals("Логин не может содержать пробелы", exception.getMessage(), "Ошибка в выбрасывании исключения");

        User user2 = new User("user2", null, 0, "email@yandex.ru", LocalDate.now());
        userController.create(user2);
        Assertions.assertEquals(userController.getUsers().get(2).getName(), "user2");
    }

    @Test
    void update() {
        User user1 = new User("login", "User1", 1,"email@yandex.ru", LocalDate.now());
        userController.create(user1);
        User user2 = new User("login", "User2", 1, "email@yandex.ru", LocalDate.now().minusDays(1));
        userController.update(user2);
        Assertions.assertEquals(userController.getUsers().get(1).getName(), "User2");
        Assertions.assertEquals(userController.getUsers().get(1).getId(), 1);
        Assertions.assertEquals(userController.getUsers().get(1).getLogin(), "login");
        Assertions.assertEquals(userController.getUsers().get(1).getBirthday(), LocalDate.now().minusDays(1));
        Assertions.assertEquals(userController.getUsers().get(1).getEmail(), "email@yandex.ru");

        User user3 = new User("login 3", "User3", 0, "email@yandex.ru", LocalDate.now().plusDays(2));
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userController.update(user3));
        assertEquals("Такого пользователя не существует", exception.getMessage(), "Ошибка в выбрасывании исключения");
        user3.setId(1);
        RuntimeException exception2 = assertThrows(
                RuntimeException.class,
                () -> userController.update(user3));
        assertEquals("Логин не может содержать пробелы", exception2.getMessage(), "Ошибка в выбрасывании исключения");

        User user4 = new User("user4", null, 1, "email@yandex.ru", LocalDate.now());
        userController.update(user4);
        Assertions.assertEquals(userController.getUsers().get(1).getName(), "user4");

    }
}