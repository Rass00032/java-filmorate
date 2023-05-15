package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.storage.mapper.FilmMapper.buildFilm;

@Slf4j
@Repository
public class FilmDbStorage implements FilmStorage {

    private static final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);

    private int id;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        id = 1;
    }

    @Override
    public Film addFilm(Film film) {

        if (film.getReleaseDate().isBefore(RELEASE_DATA)) {
            log.error("Неверный формат даты {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
        }

        if (film.getDuration() <= 0) {
            log.error("Неверная продолжительность фильма {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }
        film.setId(id++);

        jdbcTemplate.update(
                "INSERT INTO films(name, description, release_date, duration, mpa_id) VALUES( ?, ?, ?, ?,?)",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );

        return film;
    }

    @Override
    public Film updateFilm(Film film) {

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", film.getId());
        if (userRows.wasNull()) throw new ObjectNotFound("Фильм с таким id не найден!");

        if (film.getReleaseDate().isBefore(RELEASE_DATA)) {
            log.error("Неверный формат даты {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
        }

        if (film.getDuration() <= 0) {
            log.error("Неверная продолжительность фильма {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }

        jdbcTemplate.update("UPDATE films SET name = ?, description = ?," +
                        " release_date = ?, duration = ?, mpa_id = ? WHERE id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        log.info("Запись о фильме {} обновлена", film.getId());
        return film;

    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получениие списка всех фильмов");
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM films");
        List<Film> films = new ArrayList<>();

        while (userRows.next()) {
            films.add(buildFilm(userRows));
        }

        return null;//films;
    }

    @Override
    public Film getFilm(int id) {
        final Integer[] mpaId = new Integer[1];

        Film film = (Film) jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?",
                new RowMapper<Film>() {

                    @Override
                    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                        mpaId[0] = rs.getInt(6);


                        Film film = new Film(id,
                                rs.getString(2),
                                rs.getString(3),
                                rs.getDate(4).toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate(),

                                rs.getLong(5),
                                null);
                        return film;
                    }
                }, id);

        MPA mpa = (MPA) jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE id = ?", new RowMapper<MPA>() {
            @Override
            public MPA mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new MPA(rs.getInt(1), rs.getString(2));
            }
        }, mpaId[0]);
        return film;
    }

    @Override
    public boolean contains(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", id);
        if (userRows.wasNull()) return false;
        return true;
    }
}
