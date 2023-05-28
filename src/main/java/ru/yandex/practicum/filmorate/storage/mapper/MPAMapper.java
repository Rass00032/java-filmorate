package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class MPAMapper {
    public static RowMapper<MPA> rowMapperMPA() {
        return new RowMapper<MPA>() {
            @Override
            public MPA mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new MPA(rs.getInt(1), rs.getString(2));
            }
        };
    }
}
