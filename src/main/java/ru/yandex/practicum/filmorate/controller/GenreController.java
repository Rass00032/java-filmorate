package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        return service.getAllGenres();
    }

    @GetMapping(value = "/{id}")
    public Genre getGenre(@PathVariable(value = "id") int genreId) {
        return service.getGenre(genreId);
    }
}
