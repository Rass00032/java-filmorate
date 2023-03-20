package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
@AllArgsConstructor
public class User {

    private Integer id;

    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелы!")
    private String login;

    private String name;

    @Past
    private LocalDate birthday;


}
