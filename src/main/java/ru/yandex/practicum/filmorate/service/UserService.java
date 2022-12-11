package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    UserStorage userStorage;
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

    public User getUserByID(int id) {
        return userStorage.getUserByID(id);
    }

    public void addFriend(int id, int friendID) {
        userStorage.addFriend(id, friendID);
    }

    public void deleteFriend(int id, int friendID) {
        userStorage.getUserByID(id).getFriends().remove(friendID);
        userStorage.getUserByID(friendID).getFriends().remove(id);
    }

    public List<User> getFriends(int id) {
        return userStorage.getFriends(id);
    }

    public List<User> findMutualFriends(int id, int otherID) {
        if ((userStorage.getUserByID(otherID).getFriends() != null) && (userStorage.getUserByID(id).getFriends() != null)) {
            Set<Integer> otherList = userStorage.getUserByID(otherID).getFriends();
            List<User> list = new ArrayList<>();
            for (Integer u : userStorage.getUserByID(id).getFriends()) {
                if (otherList.contains(u)) {
                    list.add(userStorage.getUserByID(u));
                }
            }
            return list;
        }
        return new ArrayList<>();
    }
}
