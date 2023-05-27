package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User register(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUser(int id);

    List<User> addFriend(int userId, int friendsId);

    List<User> removeFriend(int userId, int friendsId);

    List<User> getAllFriends(int userId);

    void contains(int id);
}
