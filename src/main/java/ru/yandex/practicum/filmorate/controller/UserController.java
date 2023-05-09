package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;


import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage storage;

    private final UserService service;

    @Autowired
    UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        storage = inMemoryUserStorage;
        service = userService;
    }

    @PostMapping
    public User register(@RequestBody @Valid User user) {
        return storage.register(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return storage.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable(value = "id") int id) {
        return storage.getUser(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public List<User> addFriend(@PathVariable(value = "id") int id, @PathVariable(value = "friendId") int friendId) {
        return service.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public List<User> removeFriend(@PathVariable(value = "id") int id, @PathVariable(value = "friendId") int friendID) {
        return service.removeFriend(id, friendID);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> getAllFriends(@PathVariable(value = "id") int id) {
        return service.getAllFriends(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable(value = "id") int id, @PathVariable(value = "otherId") int otherId) {
        return service.mutualFriends(id, otherId);
    }
}
