package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private Validator validator;
    private UserController controller;
    private User user;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        factory.getConstraintValidatorFactory();
        validator = factory.getValidator();
        controller = new UserController();
        user = new User(1, "java@yandex.ru", "login", "", LocalDate.of(1995, 8, 16));
    }

    @Test
    void register() {

        controller.register(user);
        assertEquals(user.getName(), user.getLogin());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());


    }

    @Test
    void updateUser() throws ValidationException {
        controller.register(user);
        User user2 = new User(1, "java@yandex.ru", "login2", "name", LocalDate.of(1995, 8, 16));
        controller.updateUser(user2);
        String login = controller.getAllUsers().get(0).getLogin();

        assertEquals(login, user2.getLogin());
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }


}