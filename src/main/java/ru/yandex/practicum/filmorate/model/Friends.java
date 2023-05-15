package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friends {
    private Integer id;
    private Integer userId;     // userId запрашивает дружбу у friendId => userId в списке друзей friendId
    private Integer friendId;
    private boolean accepted;   // friendId принял приглашение ?
}
