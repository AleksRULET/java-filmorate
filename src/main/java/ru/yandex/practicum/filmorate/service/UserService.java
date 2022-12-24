package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserStorage userStorage;
    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getUserById(Long id) {
        return userStorage.findUserById(id).orElseThrow(() -> new ObjectNotFoundException("Пользователь с id=" + id + " не найден"));
    }

    public void addFriend(Long id, Long friendID) {
        userStorage.addFriend(id, friendID);
    }

    public void deleteFriend(Long id, Long friendID) {
        userStorage.deleteFriend(id, friendID);
    }

    public List<User> getFriends(Long id) {
        return userStorage.findFriends(id);
    }

    public List<User> getMutualFriends(Long id, Long otherID) {
        return userStorage.findMutualFriends(id, otherID);
    }

    public void delete(Long id){
        userStorage.delete(id);
    }
}
