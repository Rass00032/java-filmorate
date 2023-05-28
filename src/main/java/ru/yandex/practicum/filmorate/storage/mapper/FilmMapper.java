package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;


@NoArgsConstructor
public class FilmMapper {

    public static RowMapper<Film> rowMapperFilm() {

        return new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Film(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4).toLocalDate(),
                        rs.getInt(5),
                        new MPA(rs.getInt(6), rs.getString(7)),
                        rs.getInt(8)
                );
            }
        };
    }

    public static RowMapper<Integer> rowMapperLike() {
        return new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

                return rs.getInt(1);
            }
        };
    }
}
