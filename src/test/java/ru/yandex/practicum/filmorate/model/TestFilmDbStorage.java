package ru.yandex.practicum.filmorate.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TestFilmDbStorage {
    @Autowired
    private FilmDbStorage filmDbStorage;
    private Film film;

    @BeforeEach
    public void setUp() {
        film = new Film(1, "name", "description", LocalDate.of(2000, 10, 11), 120, new MPA(1, "G"), 2);
    }

    @Test
    public void testAddFilmAnd() throws SQLException {
        assertEquals(film, filmDbStorage.addFilm(film));
        assertEquals(film, filmDbStorage.getFilm(1));

    }

    @Test
    public void testAddAll() {
        Film film2 = new Film(2, "name2", "description", LocalDate.of(2000, 10, 11), 120, new MPA(1, "G"), 1);
        Film film3 = new Film(3, "name3", "description", LocalDate.of(2000, 10, 11), 120, new MPA(1, "G"), 2);
        filmDbStorage.addFilm(film);
        filmDbStorage.addFilm(film2);
        filmDbStorage.addFilm(film3);

        List<Film> filmList = new ArrayList<>();
        filmList.add(film);
        filmList.add(film2);
        filmList.add(film3);

        assertEquals(filmList, filmDbStorage.getAllFilms());
    }


}
