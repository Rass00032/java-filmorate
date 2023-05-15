package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Qualifier("userDbStorage")
    private UserStorage storage;

    public UserService(UserStorage inMemoryUserStorage) {
        storage = inMemoryUserStorage;
    }

    public User register(User user) {
        return storage.register(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    public User getUser(int id) {
        return storage.getUser(id);
    }

    public List<User> addFriend(int userId, int friendsId) {
        if (!storage.contains(userId))
            throw new ObjectNotFound("Пользователь с id = " + userId + " не найден.");

        if (!storage.contains(friendsId))
            throw new ObjectNotFound("Пользователь с id = " + friendsId + " не найден.");

        storage.getUser(userId).getFriends().add(friendsId);
        storage.getUser(friendsId).getFriends().add(userId);

        return getAllFriends(userId);
    }

    public List<User> removeFriend(int userId, int friendsId) {
        if (!storage.contains(userId))
            throw new ObjectNotFound("Пользователь с id = " + userId + " не найден.");

        if (!storage.contains(friendsId))
            throw new ObjectNotFound("Пользователь с id = " + friendsId + " не найден.");

        storage.getUser(userId).getFriends().remove(friendsId);
        storage.getUser(friendsId).getFriends().remove(userId);

        return getAllFriends(userId);
    }

    public List<User> getAllFriends(int id) {
        List<User> users = new ArrayList<>();

        for (Integer idFriends : storage.getUser(id).getFriends()) {
            users.add(storage.getUser(idFriends));
        }
        return users;
    }

    public List<User> mutualFriends(int userId, int anotherUserId) {
        Set<Integer> friends = new HashSet<>(storage.getUser(userId).getFriends());
        friends.retainAll(storage.getUser(anotherUserId).getFriends());
        List<User> mutualFriends = new ArrayList<>();

        if (friends.size() == 0) return mutualFriends;

        for (Integer id : friends) {
            mutualFriends.add(storage.getUser(id));
        }

        return mutualFriends;
    }
}
