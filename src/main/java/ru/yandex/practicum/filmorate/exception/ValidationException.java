package ru.yandex.practicum.filmorate.exception;

import java.io.IOException;

public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
        super(message);
    }

}
