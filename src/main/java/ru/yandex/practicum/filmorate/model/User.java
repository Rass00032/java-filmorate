package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


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

    private final Set<Integer> friends = new HashSet<>(); // содежит id друзей
}
