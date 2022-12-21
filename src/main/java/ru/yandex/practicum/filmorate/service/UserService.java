package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;

import java.util.List;

@Service
public class UserService {
    private UserStorage userStorage;
    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getUserByID(Long id) {
        User user = userStorage.getUserByID(id);
        if (user == null) {
            throw new ObjectNotFoundException("Пользователь с id=" + id + " не найден");
        }
        return userStorage.getUserByID(id);
    }

    public void addFriend(Long id, Long friendID) {
        userStorage.addFriend(id, friendID);
    }

    public void deleteFriend(Long id, Long friendID) {
        userStorage.deleteFriend(id, friendID);
    }

    public List<User> getFriends(Long id) {
        return userStorage.getFriends(id);
    }

    public List<User> findMutualFriends(Long id, Long otherID) {
        return userStorage.findMutualFriends(id, otherID);
    }

    public void delete(Long id){
        userStorage.delete(id);
    }
}
