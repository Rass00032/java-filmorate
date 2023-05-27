package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users;
    int id;

    public InMemoryUserStorage() {
        users = new HashMap<>();
        id = 1;
    }

    @Override
    public User register(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());    // если имя пустое используется логин
        User user1 = new User(id, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        users.put(user1.getId(), user1);
        id++;
        log.info("Регистрация полльзователя {}", user1.getId());
        return user1;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с таким {} не найден!", user.getId());
            throw new ObjectNotFound("Пользователь с таким id не найден!");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());    // если имя пустое используется логин
        }

        users.put(user.getId(), user);

        log.info("Учётная запись {} обновлена", user.getId());
        return user;

    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return new ArrayList<User>(users.values());
    }

    @Override
    public User getUser(int id) {
        if (!users.containsKey(id)) {
            log.error("Пользователь с таким {} не найден!", id);
            throw new ObjectNotFound("Пользователь с таким id не найден!");
        }

        return users.get(id);
    }

    @Override
    public List<User> addFriend(int userId, int friendsId) {
        contains(userId);
        contains(friendsId);

        getUser(userId).getFriends().add(friendsId);
        getUser(friendsId).getFriends().add(userId);

        return getAllFriends(userId);
    }

    @Override
    public List<User> removeFriend(int userId, int friendsId) {
        contains(userId);
        contains(friendsId);

        getUser(userId).getFriends().remove(friendsId);
        getUser(friendsId).getFriends().remove(userId);

        return getAllFriends(userId);
    }

    @Override
    public List<User> getAllFriends(int id) {
        List<User> users = new ArrayList<>();

        for (Integer idFriends : getUser(id).getFriends()) {
            users.add(getUser(idFriends));
        }
        return users;
    }

    @Override
    public void contains(int id) {
        boolean is = users.containsKey(id);
        if (!is) {
            log.error("Пользователь с таким {} не найден!", id);
            throw new ObjectNotFound("Пользователь с таким id не найден!");
        }
    }
}
