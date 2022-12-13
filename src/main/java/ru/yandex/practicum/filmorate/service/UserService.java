package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        return userStorage.getUserByID(id);
    }

    public void addFriend(Long id, Long friendID) {
        userStorage.addFriend(id, friendID);
    }

    public void deleteFriend(Long id, Long friendID) {
        userStorage.getUserByID(id).deleteFriend(friendID);
        userStorage.getUserByID(friendID).deleteFriend(id);
    }

    public List<User> getFriends(Long id) {
        return userStorage.getFriends(id);
    }

    public List<User> findMutualFriends(Long id, Long otherID) {
        if ((userStorage.getUserByID(otherID).getFriends() != null) && (userStorage.getUserByID(id).getFriends() != null)) {
            Set<Long> otherList = userStorage.getUserByID(otherID).getFriends();
            List<User> list = new ArrayList<>();
            for (Long u : userStorage.getUserByID(id).getFriends()) {
                if (otherList.contains(u)) {
                    list.add(userStorage.getUserByID(u));
                }
            }
            return list;
        }
        return new ArrayList<>();
    }

    public void delete(Long id){
        userStorage.delete(id);
    }
}
