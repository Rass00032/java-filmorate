package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films;

    private static final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);

    private int id;


    public InMemoryFilmStorage() {
        films = new HashMap<>();
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
        films.put(film.getId(), film);

        log.info("Добавлен фильм {}", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) throw new ObjectNotFound("Фильм с таким id не найден!");

        if (film.getReleaseDate().isBefore(RELEASE_DATA)) {
            log.error("Неверный формат даты {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
        }

        if (film.getDuration() <= 0) {
            log.error("Неверная продолжительность фильма {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }

        films.put(film.getId(), film);

        log.info("Запись о фильме {} обновлена", film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получениие списка всех фильмов");
        return new ArrayList<Film>(films.values());
    }

    @Override
    public Film getFilm(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new ObjectNotFound("Фильм с id = " + id + " не найден.");
        }
    }

    @Override
    public List<Film> findPopularFilms(int count) {
        Comparator<Film> byLikes = Comparator.comparingInt(o -> o.getLike().size());
        return getAllFilms().stream()
                .sorted(byLikes.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public int removeLike(int filmId, int userId) {
        return 0;
    }

    @Override
    public int addLike(int filmId, int userId) {
        return 0;
    }

    @Override
    public void contains(int id) {
        if (!films.containsKey(id)) {
            throw new ObjectNotFound("Фильм с id = " + id + " не найден.");
        }
    }
}
