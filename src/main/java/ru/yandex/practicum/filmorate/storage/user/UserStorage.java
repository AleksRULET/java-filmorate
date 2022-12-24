package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAll();
    User create(User user);
    User update(User user);
    Optional<User> findUserById(Long id);

    void addFriend(Long id, Long otherId);
    List<User> findFriends(Long id);
    void delete(Long id);
    List<User> findMutualFriends(Long id, Long otherID);
    void deleteFriend(Long id, Long friendID);
}
