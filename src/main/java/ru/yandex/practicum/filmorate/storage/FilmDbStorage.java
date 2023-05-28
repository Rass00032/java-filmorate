package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ru.yandex.practicum.filmorate.storage.mapper.FilmMapper.rowMapperFilm;
import static ru.yandex.practicum.filmorate.storage.mapper.FilmMapper.rowMapperLike;
import static ru.yandex.practicum.filmorate.storage.mapper.GenreMapper.rowMapperGenre;


@Slf4j
@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private static final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);

    private final GenreDbStorage genreDbStorage;

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(GenreDbStorage genreDbStorage, JdbcTemplate jdbcTemplate) {
        this.genreDbStorage = genreDbStorage;
        this.jdbcTemplate = jdbcTemplate;
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

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO films(name, description, release_date, duration, mpa_id, rate) VALUES( ?, ?, ?, ?, ?, ?)", new String[]{"id"});
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            preparedStatement.setInt(4, film.getDuration());
            preparedStatement.setInt(5, film.getMpa().getId());
            if (film.getRate() == null) {
                preparedStatement.setInt(6, 0);
            } else {
                preparedStatement.setInt(6, film.getRate());
            }
            return preparedStatement;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());

        for (Integer id : film.getLike()) {
            jdbcTemplate.update(
                    "INSERT INTO LIKES (film_id, user_id) VALUES (?, ?)",
                    film.getId(),
                    id
            );
        }

        List<Genre> genres = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(
                    "INSERT INTO films_genres  (film_id, genre_id) VALUES (?, ?)",
                    film.getId(),
                    genre.getId()
            );
            genres.add(genreDbStorage.getGenre(genre.getId()));
        }

        film.getGenres().clear();
        for (Genre genre : genres) {
            film.getGenres().add(genre);
        }

        log.info("Фильм {} добавлен", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        contains(film.getId());

        if (film.getReleaseDate().isBefore(RELEASE_DATA)) {
            log.error("Неверный формат даты {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
        }

        if (film.getDuration() <= 0) {
            log.error("Неверная продолжительность фильма {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }

        jdbcTemplate.update("UPDATE films SET name = ?, description = ?," +
                        " release_date = ?, duration = ?, mpa_id = ?, rate = ? WHERE id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRate(),
                film.getId()
        );

        updateLikeFilm(film);

        List<Genre> genres = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(
                    "INSERT INTO films_genres  (film_id, genre_id) VALUES (?, ?)",
                    film.getId(),
                    genre.getId()
            );
            genres.add(genreDbStorage.getGenre(genre.getId()));
        }

        film.getGenres().clear();
        genres.sort(new Comparator<Genre>() {
            @Override
            public int compare(Genre o1, Genre o2) {
                return o1.getId() - o2.getId();
            }
        });

        for (Genre genre : genres) {
            film.getGenres().add(genre);
        }

        updateGenreFilm(film);

        log.info("Запись о фильме {} обновлена", film.getId());
        return film;

    }

    private void updateGenreFilm(Film film) {

        jdbcTemplate.update("DELETE FROM films_genres WHERE film_id = ?", film.getId());

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(
                    "INSERT INTO films_genres  (film_id, genre_id) VALUES (?, ?)",
                    film.getId(),
                    genre.getId()
            );
        }
    }

    private void updateLikeFilm(Film film) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ?", film.getId());

        jdbcTemplate.update("UPDATE films " +
                "SET rate = ? " +
                "WHERE id = ? ", film.getRate(), film.getId());

        for (Integer id : film.getLike()) {
            addLike(film.getId(), id);
        }
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получениие списка всех фильмов");

        List<Integer> listId = jdbcTemplate.queryForList("SELECT id FROM films ORDER BY id", Integer.class);

        List<Film> films = new ArrayList<>();
        for (Integer id : listId) {
            films.add(getFilm(id));
        }

        return films;
    }

    @Override
    public Film getFilm(int id) {
        List<Film> film = jdbcTemplate.query("SELECT f.id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "m.id, " +
                "m.name, " +
                "f.rate " +
                "FROM films AS f " +
                "INNER JOIN mpa AS m ON m.id = f.mpa_id " +
                "WHERE f.id = ?", rowMapperFilm(), id);

        if (film.size() != 1) throw new ObjectNotFound("Фильм с id - " + id + " не найден.");

        List<Integer> likes = jdbcTemplate.query("SELECT u.id " +
                "FROM films AS f " +
                "JOIN likes AS l ON l.film_id  = f.id " +
                "JOIN users AS u ON u.id = l.user_id " +
                "WHERE f.id = ?;", rowMapperLike(), id);

        for (Integer likeId : likes) {
            film.get(0).getLike().add(likeId);
        }

        List<Genre> genres = jdbcTemplate.query("SELECT g.id, g.name " +
                "FROM films AS f " +
                "JOIN films_genres AS fg ON fg.film_id = f.id " +
                "JOIN genres AS g ON fg.genre_id = g.id " +
                "WHERE f.id = ? " +
                "ORDER BY g.id ", rowMapperGenre(), id);

        for (Genre genre : genres) {
            film.get(0).getGenres().add(genre);
        }

        return film.get(0);
    }

    @Override
    public List<Film> findPopularFilms(int number) {

        List<Integer> idList = jdbcTemplate.queryForList("SELECT f.id " +
                "FROM films AS f " +
                "INNER JOIN likes AS l on f.id = l.film_id " +
                "GROUP BY f.id " +
                "ORDER BY COUNT(l.id) DESC " +
                "LIMIT ?;", Integer.class, number);

        if (idList.size() == 0) {
            idList = jdbcTemplate.queryForList("SELECT id FROM films", Integer.class);
        }

        List<Film> rating = new ArrayList<>();
        for (Integer id : idList) {
            rating.add(getFilm(id));
        }
        return rating;
    }

    @Override
    public int removeLike(int filmId, int userId) {
        Film film = getFilm(filmId);
        film.getLike().remove(userId);

        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ?", film.getId(), userId);

        return film.getLike().size();
    }

    @Override
    public int addLike(int filmId, int userId) {
        Film film = getFilm(filmId);
        film.getLike().add(userId);


        jdbcTemplate.update(
                "INSERT INTO LIKES (film_id, user_id) VALUES (?, ?)",
                film.getId(),
                userId
        );

        return film.getLike().size();
    }

    @Override
    public void contains(int id) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM films WHERE id = ?", Integer.class, id);
        if (result == 0) throw new ObjectNotFound("Фильм с id - " + id + " не найден.");
    }
}

