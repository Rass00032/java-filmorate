package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.storage.mapper.UserMapper.rowMapperUser;

@Slf4j
@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User register(User user) {

        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());    // если имя пустое используется логин

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(" +
                    "email, " +
                    "login, " +
                    "name, " +
                    "birthday" +
                    ") VALUES( ?, ?, ?, ?)", new String[]{"id"});
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));

            return preparedStatement;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());

        for (Integer id : user.getFriends()) {
            jdbcTemplate.update(
                    "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)",
                    user.getId(),
                    id
            );
        }

        log.info("Учётная запись {} добавлена", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {

        contains(user.getId());

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());    // если имя пустое используется логин
        }

        jdbcTemplate.update("UPDATE users SET " +
                        "email = ?, " +
                        "login = ?, " +
                        "name = ?, " +
                        "birthday  = ? WHERE id = ?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );

        log.info("Учётная запись {} обновлена", user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {

        log.info("Получениие списка всех пользователей");

        List<Integer> listId = jdbcTemplate.queryForList("SELECT id FROM users", Integer.class);

        List<User> users = new ArrayList<>();
        for (Integer id : listId) {
            users.add(getUser(id));
        }

        return users;
    }

    @Override
    public User getUser(int id) {

        List<User> user = jdbcTemplate.query("SELECT * " +
                "FROM users " +
                "WHERE id = ?", rowMapperUser(), id);

        if (user.size() != 1) throw new ObjectNotFound("Пользователь с id - " + id + " не найден.");

        List<Integer> friendsId = jdbcTemplate.queryForList("SELECT friend_id " +
                "FROM friends " +
                "WHERE user_id = ?", Integer.class, id);

        for (Integer friendId : friendsId) {
            user.get(0).getFriends().add(friendId);
        }

        return user.get(0);
    }

    @Override
    public List<User> addFriend(int userId, int friendsId) {
       contains(userId);
       contains(friendsId);

        jdbcTemplate.update("INSERT INTO friends(user_id, friend_id) VALUES( ?, ?)", userId, friendsId);

        return getAllFriends(userId);
    }

    @Override
    public List<User> removeFriend(int userId, int friendsId) {
        contains(userId);
        contains(friendsId);

        jdbcTemplate.update("DELETE FROM friends" +
                " WHERE user_id = ? AND friend_id = ?", userId, friendsId);

        return getAllFriends(userId);
    }

    @Override
    public List<User> getAllFriends(int userId) {
       contains(userId);

        List<Integer> usersId = jdbcTemplate.queryForList("SELECT friend_id " +
                "FROM friends WHERE user_id = ?", Integer.class, userId);

        List<User> users = new ArrayList<>();

        for (Integer id : usersId) {
            users.add(getUser(id));
        }

        return users;
    }

    @Override
    public void contains(int id) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE id = ?", Integer.class, id);
        if (result == 0) {
            log.error("Пользователь с таким {} не найден!", id);
            throw new ObjectNotFound("Пользователь с таким id не найден!");
        }
    }
}
