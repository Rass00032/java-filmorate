package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static ru.yandex.practicum.filmorate.storage.mapper.GenreMapper.rowMapperGenre;

@Slf4j
@Component
@Qualifier("genreDbStorage")
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM genres", rowMapperGenre());
        return genres;
    }

    @Override
    public Genre getGenre(int genreId) {
        int exists = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM genres WHERE id = ?", Integer.class, genreId);
        if(exists == 0) throw new ObjectNotFound("Объект " +genreId +" не найден");

        return jdbcTemplate.queryForObject("SELECT * FROM genres WHERE id = ?", rowMapperGenre(), genreId);
    }
}
