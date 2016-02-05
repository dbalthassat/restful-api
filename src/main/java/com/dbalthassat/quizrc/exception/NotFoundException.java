package com.dbalthassat.quizrc.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpStatusException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException() {
        this("Item not found");
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
