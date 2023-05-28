package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friends {
    private Integer id;
    private Integer userId;
    private Integer friendId;
}
