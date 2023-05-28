package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.util.List;

@Service
public class FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage storage;

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        storage = inMemoryFilmStorage;
        userStorage = inMemoryUserStorage;
    }

    public Film addFilm(Film film) {
        return storage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return storage.updateFilm(film);
    }

    public Film getFilm(int id) {
        return storage.getFilm(id);
    }

    public List<Film> getAllFilms() {
        return storage.getAllFilms();
    }

    public int addLike(int filmId, int userId) {
        storage.contains(filmId);
        userStorage.contains(userId);
        return storage.addLike(filmId,userId);
    }

    public int removeLike(int filmId, int userId) {
        storage.contains(filmId);
        userStorage.contains(userId);
        return storage.removeLike(filmId,userId);
    }

    public List<Film> findPopularFilms(int count) {
        return storage.findPopularFilms(count);
    }
}
