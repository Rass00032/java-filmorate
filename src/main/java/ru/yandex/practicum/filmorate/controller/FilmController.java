package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    FilmController(FilmService filmService) {
        service = filmService;
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return service.updateFilm(film);
    }

    @GetMapping(value = "/{id}")
    private Film getFilm(@PathVariable(value = "id") int id) {
        return service.getFilm(id);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return service.getAllFilms();
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public int addLike(@PathVariable(value = "id") int id, @PathVariable(value = "userId") int userId) {
        return service.addLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public int removeLike(@PathVariable(value = "id") int id, @PathVariable(value = "userId") int userId) {
        return service.removeLike(id, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> popularFilm(@RequestParam(defaultValue = "10", required = false) String count) {
        return service.findPopularFilms(Integer.parseInt(count));
    }
}
