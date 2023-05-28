package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Genre {
    private Integer id;

    @NotBlank(message = "Строка не может быть пустой.")
    private String name;

}
