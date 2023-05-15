package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {

    private final FilmController controller;
    private Film film;



    @BeforeEach
    public void setUp() {
        film = new Film(1, "name", "description", LocalDate.of(2000, 10, 11), 120L, new MPA(1,"G"));
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
        Film film2 = new Film(film.getId(), "name", "description", LocalDate.of(2000, 10, 11), 0L,new MPA(1,"G"));

        Exception exception = assertThrows(Exception.class, () -> {
            controller.updateFilm(film2);
        });
        assertEquals("Продолжительность фильма должна быть положительной!", exception.getMessage());
    }
}