package ru.yandex.practicum.filmorate.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TestUserDbStorage {
    private final UserDbStorage storage;
    private User user1;

    @BeforeEach
    public void setUp() {
        user1 = new User(1, "email.@mail.ru", "login1", "name1", LocalDate.of(2000, 10, 11));
    }

    @Test
    public void testAddUser() throws SQLException {
        assertEquals(user1, storage.register(user1));
        assertEquals(user1, storage.getUser(1));

    }

    @Test
    public void testAddAll() {
        User user2 = new User(2, "email2.@mail.ru", "login2", "name2", LocalDate.of(2002, 12, 11));
        User user3 = new User(3, "email3.@mail.ru", "login3", "name3", LocalDate.of(2003, 10, 11));

        storage.register(user2);
        storage.register(user3);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        assertEquals(users, storage.getAllUsers());
    }
}
