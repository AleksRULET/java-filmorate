package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        log.debug("Текущее количество пользователей: {}", userService.getAll().size());
        return userService.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Добавлен пользователь {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Обновлён пользователь {}", user);
        return userService.update(user);
    }

    @GetMapping(value = "{id}")
    public User getUserByID(@PathVariable Long id) {
        return userService.getUserByID(id);
    }

    @PutMapping(value = "{id}/friends/{friendID}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendID) {
        userService.addFriend(id, friendID);
        log.debug("Добавлен в друзья {}", userService.getUserByID(friendID));
        return userService.getUserByID(friendID);
    }

    @DeleteMapping(value = "{id}/friends/{friendID}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendID) {
        userService.deleteFriend(id, friendID);
        log.debug("Удален из друзей {}", userService.getUserByID(friendID));

    }

    @GetMapping(value = "{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    @GetMapping(value = "{id}/friends/common/{otherID}")
    public List<User> findMutualFriends(@PathVariable Long id, @PathVariable Long otherID) {
        return userService.findMutualFriends(id, otherID);
    }
}
