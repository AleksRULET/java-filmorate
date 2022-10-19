package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    private HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (!user.getLogin().contains(" ")) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            user.setId(id);
            id++;
            log.debug("Добавлен пользователь {}", user);
            users.put(user.getId(), user);
            return user;
        }
        throw new ValidationException("Логин не может содержать пробелы");
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            if (!user.getLogin().contains(" ")) {
                if (user.getName() == null) {
                    user.setName(user.getLogin());
                }
                users.put(user.getId(), user);
                log.debug("Обновлён пользователь {}", user);
                return user;
            }
            throw new ValidationException("Логин не может содержать пробелы");
        }
        throw new ValidationException("Такого пользователя не существует");
    }
}
