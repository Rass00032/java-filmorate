package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
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
    public boolean contains(int id) {
        boolean is = users.containsKey(id);
        if (!is) log.error("Пользователь с таким {} не найден!", id);
        return is;
    }
}
