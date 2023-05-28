package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    UserController(UserService userService) {
        service = userService;
    }

    @PostMapping
    public User register(@RequestBody @Valid User user) {
        return service.register(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return service.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable(value = "id") int id) {
        return service.getUser(id);
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
