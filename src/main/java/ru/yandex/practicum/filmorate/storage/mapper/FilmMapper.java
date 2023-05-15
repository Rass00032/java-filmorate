package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;


@NoArgsConstructor
public class FilmMapper {

    public static Film buildFilm(SqlRowSet rs) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        Integer mpaId = rs.getInt("MPA_ID");

        String mpaName = jdbcTemplate.queryForRowSet("SELECT name FROM mpa WHERE id = ?",mpaId)
                .getString("NAME");

        MPA mpa = new MPA(mpaId,mpaName);



        return new Film(rs.getInt("ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getLong("DURATION"),
                mpa
                );
    }

    private static Genre buildGenre(SqlRowSet rs) {
        return new Genre(rs.getInt("genre_id"), rs.getString("GENRE_NAME"));
    }
}
