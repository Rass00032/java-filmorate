package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {

    private Integer id;

    @NotBlank
    private String name;

    @Pattern(regexp = "^[\\s\\S]{0,200}$", message = "Описание должно быть не более 200 знаков!")
    private String description;

    private LocalDate releaseDate;

    private Long duration;

    private final Set<Integer> like = new HashSet<>();

}
