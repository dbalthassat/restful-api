package com.dbalthassat.quizrc.exception;

import org.springframework.http.HttpStatus;

public class NotQueryableException extends HttpStatusException {
    public NotQueryableException(String message) {
        super(message);
    }

    public NotQueryableException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    HttpStatus httpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
