package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FilmService {
    private InMemoryFilmStorage storage;
    private InMemoryUserStorage userStorage;


    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        storage = inMemoryFilmStorage;
        userStorage = inMemoryUserStorage;
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
