package ru.yandex.practicum.filmorate.storage.users;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();
    User create(User user);
    User update(User user);
    User getUserByID(int id);

    void addFriend(int id, int otherId);
    List<User> getFriends(int id);
}
