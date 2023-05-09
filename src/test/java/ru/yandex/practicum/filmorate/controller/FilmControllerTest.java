package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootApplication
class FilmControllerTest {
    private FilmController controller;
    private InMemoryFilmStorage storage;
    private InMemoryUserStorage userStorage;
    private FilmService service;
    private Film film;

    @BeforeEach
    public void setUp() {
        storage = new InMemoryFilmStorage();
        service = new FilmService(storage, userStorage);
        controller= new FilmController(storage,service);
        film = new Film(1, "name", "description", LocalDate.of(2000, 10, 11), 120L);
    }

    @Test
    void addFilm() {
        film.setReleaseDate(LocalDate.of(1852, 12, 30));

        Exception exception = assertThrows(Exception.class, () -> {
            controller.addFilm(film);
        });
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года!", exception.getMessage());
    }

    @Test
    void updateFilm() throws ValidationException {
        controller.addFilm(film);
        Film film2 = new Film(film.getId(), "name", "description", LocalDate.of(2000, 10, 11), 0L);

        Exception exception = assertThrows(Exception.class, () -> {
            controller.updateFilm(film2);
        });
        assertEquals("Продолжительность фильма должна быть положительной!", exception.getMessage());
    }
}