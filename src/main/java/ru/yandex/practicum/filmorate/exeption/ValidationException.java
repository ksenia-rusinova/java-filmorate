package ru.yandex.practicum.filmorate.exeption;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {
    private final HttpStatus status;

    public ValidationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
