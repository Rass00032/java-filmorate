package ru.yandex.practicum.filmorate.controller;

import java.io.IOException;

public class ValidationException extends IOException {
    public ValidationException(final String message) {
        super(message);
    }

}
