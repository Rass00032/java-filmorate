package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilm(int id);

    List<Film> findPopularFilms(int count);

    int removeLike(int filmId, int userId);

    int addLike(int filmId, int userId);

    void contains(int id);
}
