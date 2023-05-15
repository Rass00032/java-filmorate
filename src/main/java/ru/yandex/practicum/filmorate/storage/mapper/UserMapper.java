package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.NoArgsConstructor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class UserMapper {
    public static List<User> extractorUser(SqlRowSet rs) {
        Map<Integer, User> usersById = new HashMap<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            User user = usersById.get(id);
            if (user == null) {
                user = buildUser(rs, id);
                usersById.put(user.getId(), user);
            }
        }
        return new ArrayList<>(usersById.values());
    }

    private static User buildUser(SqlRowSet rs, int id) {
        return new User(id, rs.getString("email"), rs.getString("login"),
                rs.getString("name"), rs.getDate("birthday").toLocalDate());
    }
}
