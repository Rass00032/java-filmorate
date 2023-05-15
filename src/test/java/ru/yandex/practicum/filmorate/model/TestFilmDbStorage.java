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

    private final FilmDbStorage filmDbStorage;
    private Film film;

    @BeforeEach
    public void setUp() {
        film = new Film(1, "name", "description", LocalDate.of(2000, 10, 11), 120L, new MPA(1,"G"));
    }

    @Test
    public void testAddFilmAnd() throws SQLException {
        List<Film> filmList = new ArrayList<>();
        filmList.add(film);
        assertEquals(film,filmDbStorage.addFilm(film));
        assertEquals(filmList,filmDbStorage.getFilm(1));

    }


}
