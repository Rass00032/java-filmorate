package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FilmService {

    private FilmStorage storage;
    private UserStorage userStorage;

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

    public int addLike(int filmId, int userId){
        userStorage.getUser(userId);
        storage.getFilm(filmId).getLike().add(userId);

        return storage.getFilm(filmId).getLike().size();
    }

    public int removeLike(int filmId, int userId){
        userStorage.getUser(userId);
        storage.getFilm(filmId).getLike().remove(userId);

        return storage.getFilm(filmId).getLike().size();
    }

    public List<Film> findPopularFilms(int count) {
        Comparator<Film> byLikes = Comparator.comparingInt(o -> o.getLike().size());
        return storage.getAllFilms().stream()
                .sorted(byLikes.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
