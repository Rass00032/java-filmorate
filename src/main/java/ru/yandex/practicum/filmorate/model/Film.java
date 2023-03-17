package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.*;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;

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


}
