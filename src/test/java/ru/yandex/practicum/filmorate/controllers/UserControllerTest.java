package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.users.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;
    @BeforeEach
    void init() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }
    @Test
    void getAll() {
        Assertions.assertEquals(0, userController.getAll().size());
        userController.create(new User("login", "User1",0L, "email@yandex.ru", LocalDate.of(2000,12,12), Set.of(1L)));
        userController.create(new User("login", "User2", 0L, "email@yandex.ru", LocalDate.of(2000,12,12), Set.of(1L)));
        Collection<User> users =  userController.getAll();
        Assertions.assertEquals(2, users.size());
        Assertions.assertEquals(users.toArray()[1].toString(), "User(login=login, name=User2, id=2, email=email@yandex.ru, birthday=2000-12-12, friends=[])");
    }

    @Test
    void create() {
        User user1 = new User("login", "User1", 0L, "email@yandex.ru", LocalDate.of(2000,12,12), Set.of(1L));
        userController.create(user1);
        Assertions.assertEquals(userController.getAll().get(0).getName(), "User1");
        Assertions.assertEquals(userController.getAll().get(0).getId(), 1);
        Assertions.assertEquals(userController.getAll().get(0).getLogin(), "login");
        Assertions.assertEquals(userController.getAll().get(0).getBirthday(), LocalDate.of(2000,12,12));
        Assertions.assertEquals(userController.getAll().get(0).getEmail(), "email@yandex.ru");

        user1.setLogin("User 1");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userController.create(user1));
        assertEquals("?????????? ???? ?????????? ?????????????????? ??????????????", exception.getMessage(), "???????????? ?? ???????????????????????? ????????????????????");

        User user2 = new User("user2", null, 0L, "email@yandex.ru", LocalDate.of(2000,12,12), Set.of(1L));
        userController.create(user2);
        Assertions.assertEquals(userController.getAll().get(1).getName(), "user2");
    }

    @Test
    void update() {
        User user1 = new User("login", "User1", 1L,"email@yandex.ru", LocalDate.of(2000,12,12), Set.of(1L));
        userController.create(user1);
        User user2 = new User("login", "User2", 1L, "email@yandex.ru", LocalDate.of(2000,12,12), Set.of(1L));
        userController.update(user2);
        Assertions.assertEquals(userController.getAll().get(0).getName(), "User2");
        Assertions.assertEquals(userController.getAll().get(0).getId(), 1);
        Assertions.assertEquals(userController.getAll().get(0).getLogin(), "login");
        Assertions.assertEquals(userController.getAll().get(0).getBirthday(), LocalDate.of(2000,12,12));
        Assertions.assertEquals(userController.getAll().get(0).getEmail(), "email@yandex.ru");

        User user3 = new User("login 3", "User3", 0L, "email@yandex.ru", LocalDate.of(2000,12,12), Set.of(1L));
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userController.update(user3));
        assertEquals("???????????? ???????????????????????? ???? ????????????????????", exception.getMessage(), "???????????? ?? ???????????????????????? ????????????????????");
        user3.setId(1L);
        RuntimeException exception2 = assertThrows(
                RuntimeException.class,
                () -> userController.update(user3));
        assertEquals("?????????? ???? ?????????? ?????????????????? ??????????????", exception2.getMessage(), "???????????? ?? ???????????????????????? ????????????????????");

        User user4 = new User("user4", null, 1L, "email@yandex.ru", LocalDate.of(2000,12,12), Set.of(1L));
        userController.update(user4);
        Assertions.assertEquals(userController.getAll().get(0).getName(), "user4");

    }
}