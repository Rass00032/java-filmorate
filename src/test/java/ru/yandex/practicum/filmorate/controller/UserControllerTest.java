package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;


import javax.validation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootApplication
class UserControllerTest {

    private UserController controller;

    private Validator validator;

    private InMemoryUserStorage storage;

    private UserService service;
    private User user;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        factory.getConstraintValidatorFactory();
        validator = factory.getValidator();

        storage = new InMemoryUserStorage();
        service = new UserService(storage);
        controller = new UserController(storage, service);
        user = new User(1, "java@yandex.ru", "login", "", LocalDate.of(1995, 8, 16));
    }

    @Test
    void register() throws ru.yandex.practicum.filmorate.exception.ValidationException {

        controller.register(user);
        assertEquals(user.getName(), user.getLogin());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void updateUser() throws ru.yandex.practicum.filmorate.exception.ValidationException {
        controller.register(user);
        User user2 = new User(1, "java@yandex.ru", "login2", "name", LocalDate.of(1995, 8, 16));
        controller.updateUser(user2);
        String login = controller.getAllUsers().get(0).getLogin();

        assertEquals(login, user2.getLogin());
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }


    @Test
    void addFriendAndRemove() throws ru.yandex.practicum.filmorate.exception.ValidationException {
        user = controller.register(user);

        User user2 = new User(null, "java2@yandex.ru", "login2", "name", LocalDate.of(1995, 8, 16));
        user2 = controller.register(user2);

        controller.addFriend(user.getId(), user2.getId());

        assertEquals(user2, controller.getAllFriends(user.getId()).get(0));

        controller.removeFriend(user.getId(), user2.getId());
        assertEquals(0, controller.getAllFriends(user.getId()).size());
    }

    @Test
    void findCommonFriends() throws ValidationException {

        User user2 = new User(2, "java2@yandex.ru", "login2", "name",
                LocalDate.of(1995, 8, 16));

        User user3 = new User(3, "java3@yandex.ru", "login3", "name",
                LocalDate.of(2000, 10, 6));

        user = controller.register(user);
        user2 = controller.register(user2);
        user3 = controller.register(user3);

        controller.addFriend(user.getId(), user3.getId());
        controller.addFriend(user2.getId(), user3.getId());

        List<User> answer = new ArrayList<>();
        answer.add(user3);

        assertEquals(answer, controller.findCommonFriends(user.getId(), user2.getId()));
    }

}