package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    @Qualifier("genreDbStorage")
    private final GenreStorage storage;

    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    public List<Genre> getAllGenres() {
        return storage.getAllGenres();
    }

    public Genre getGenre(int genreId) {
        return storage.getGenre(genreId);
    }
}
