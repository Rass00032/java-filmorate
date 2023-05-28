package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MPAController {
    private final MPAService service;

    public MPAController(MPAService service) {
        this.service = service;
    }

    @GetMapping
    public List<MPA> getAllMPA() {
        return service.getAllMPA();
    }

    @GetMapping(value = "/{id}")
    public MPA getMPA(@PathVariable(value = "id") int id) {
        return service.getMPA(id);
    }
}
