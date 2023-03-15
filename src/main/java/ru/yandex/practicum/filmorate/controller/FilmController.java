package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();

    private final static LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);

    private int id = 1;

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) throws ValidationException {    //добавление фильма


        if (film.getReleaseDate().isBefore(RELEASE_DATA)) {
            log.error("Неверный формат даты {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
        }

        if (film.getDuration() <= 0) {
            log.error("Неверная продолжительность фильма {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }

        film.setId(id++);
        films.put(film.getId(), film);

        log.info("Добавлен фильм {}", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) throws ValidationException {     //обновление фильма
        if (!films.containsKey(film.getId())) throw new ValidationException("Фильм с таким id не найден!");

        if (film.getReleaseDate().isBefore(RELEASE_DATA)) {
            log.error("Неверный формат даты {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
        }

        if (film.getDuration() <= 0) {
            log.error("Неверная продолжительность фильма {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }

        films.put(film.getId(), film);

        log.info("Запись о фильме {} обновлена", film.getId());
        return film;
    }

    @GetMapping
    public List<Film> getAllUsers() throws ValidationException {    //получение всех фильмов
        log.info("Получениие списка всех фильмов");
        return new ArrayList<Film>(films.values());
    }


}
