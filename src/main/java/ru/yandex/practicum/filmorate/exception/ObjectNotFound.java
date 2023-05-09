package ru.yandex.practicum.filmorate.exception;

import java.io.IOException;

public class ObjectNotFound extends RuntimeException {

    public ObjectNotFound(final String message){
        super(message);
    }

}
