package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class GenreMapper {
    public static RowMapper<Genre> rowMapperGenre() {
        return new RowMapper<Genre>() {
            @Override
            public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {

                return new Genre(rs.getInt(1), rs.getString(2));
            }
        };
    }
}
