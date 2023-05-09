package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private InMemoryUserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        storage = inMemoryUserStorage;
    }

    public List<User> addFriend(int userId, int friendsId){
        if (!storage.contains(userId))
            throw new ObjectNotFound("Пользователь с id = " + userId + " не найден.");

        if (!storage.contains(friendsId))
            throw new ObjectNotFound("Пользователь с id = " + friendsId + " не найден.");

        storage.getUser(userId).getFriends().add(friendsId);
        storage.getUser(friendsId).getFriends().add(userId);

        return getAllFriends(userId);
    }

    public List<User> removeFriend(int userId, int friendsId){
        if (!storage.contains(userId))
            throw new ObjectNotFound("Пользователь с id = " + userId + " не найден.");

        if (!storage.contains(friendsId))
            throw new ObjectNotFound("Пользователь с id = " + friendsId + " не найден.");

        storage.getUser(userId).getFriends().remove(friendsId);
        storage.getUser(friendsId).getFriends().remove(userId);

        return getAllFriends(userId);
    }

    public List<User> getAllFriends(int id){
        Object[] friends = storage.getUser(id).getFriends().toArray();
        List<User> users = new ArrayList<>();
        for (Object friendsId : friends) {
            users.add(storage.getUser((int)friendsId));
        }
        return users;
    }

    public List<User> mutualFriends(int userId, int anotherUserId){
        List<User> friendsUser = getAllFriends(userId);
        Set<Integer> friendsAnotherUser = storage.getUser(anotherUserId).getFriends();
        List<User> mutualFriends = new ArrayList<>();

        if(friendsUser.size() == 0) return mutualFriends;

        for (User user : friendsUser) {
            if (friendsAnotherUser.contains(user.getId())) {
                mutualFriends.add(user);
            }
        }

        return mutualFriends;
    }
}
