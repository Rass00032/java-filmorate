package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private Map<Integer, User> users = new HashMap<>();

    private int id = 1;

    @PostMapping
    public User register(@RequestBody @Valid User user) {    //создание пользователя
        user.setId(id++);
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());    // если имя пустое используется логин
        users.put(user.getId(), user);
        log.info("Регистрация полльзователя {}",user.getId());
        return user;

    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) throws ValidationException {     //обновление пользователя
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с таким {} не найден!",user.getId());
            throw new ValidationException("Пользователь с таким id не найден!");
        }

        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());    // если имя пустое используется логин
        users.put(user.getId(), user);

        log.info("Учётная запись {} обновлена", user.getId());
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {    //получение списка всех пользователей
        log.info("Получение списка всех пользователей");
        return new ArrayList<User>( users.values());
    }


}
