package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController controller;
    private Film film;

    @BeforeEach
    public void setUp() {
        controller = new FilmController();
        film = new Film(1,"name","description", LocalDate.of(2000,10,11), Duration.ofMinutes(120));
    }
    @Test
    void addFilm() {
        film.setReleaseDate(LocalDate.of(1852,12,30));

        Exception exception = assertThrows(Exception.class, () -> {
            controller.addFilm(film);
        });
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года!", exception.getMessage());
    }

    @Test
    void updateUser() throws ValidationException {
        controller.addFilm(film);
        Film film2 = new Film(film.getId(),"name","description", LocalDate.of(2000,10,11), Duration.ofMinutes(0));

        Exception exception = assertThrows(Exception.class, () -> {
            controller.updateFilm(film2);
        });
        assertEquals("Продолжительность фильма должна быть положительной!", exception.getMessage());
    }

}