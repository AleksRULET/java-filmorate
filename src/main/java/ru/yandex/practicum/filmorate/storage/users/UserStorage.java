package ru.yandex.practicum.filmorate.storage.users;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface UserStorage {
    List<User> getAll();
    User create(User user);
    User update(User user);
    User getUserByID(Long id);

    void addFriend(Long id, Long otherId);
    List<User> getFriends(Long id);
    void delete(Long id);
    List<User> findMutualFriends(Long id, Long otherID);
    void deleteFriend(Long id, Long friendID);
}
