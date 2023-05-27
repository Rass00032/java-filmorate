package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class UserMapper {

    public static RowMapper<User> rowMapperUser() {

        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5).toLocalDate()
                );
            }
        };
    }
}
