package ru.yandex.practicum.filmorate.storage.users;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private HashMap<Integer, User> users = new HashMap<>();

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
        if (user != null) {
            if (!user.getLogin().contains(" ")) {
                if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank() ) {
                    user.setName(user.getLogin());
                }
                user.setId(id);
                id++;
                user.setFriends(new HashSet<>());
                users.put(user.getId(), user);
                return user;
            }
            throw new ValidationException("Логин не может содержать пробелы");
        }
        throw new RuntimeException();
    }

    public User update(User user) {
        if (users.containsKey(user.getId())) {
            if (!user.getLogin().contains(" ")) {
                if (user.getName() == null) {
                    user.setName(user.getLogin());
                }
                users.put(user.getId(), user);
                return user;
            }
            throw new ValidationException("Логин не может содержать пробелы");
        }
        throw new ObjectNotFoundException("Такого пользователя не существует");
    }

    public User getUserByID(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new ObjectNotFoundException("Пользователь не найден");
    }

    public void addFriend(int id, int otherId) {
        if (users != null) {
            if (users.containsKey(id) && users.containsKey(otherId)) {
                users.get(id).addFriend(otherId);
                users.get(otherId).addFriend(id);
                return;
            }
            throw new ObjectNotFoundException("Такого пользователя не существует");
        }
    }

    public List<User> getFriends(int id) {
        if (users.containsKey(id)) {
            List<User> friends = new ArrayList<>();
            for (Integer u: users.get(id).getFriends()) {
                friends.add(users.get(u));
            }
            return friends;
        }
        throw new ObjectNotFoundException("Пользователь не найден");
    }
}
