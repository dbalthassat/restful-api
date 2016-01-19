package com.dbalthassat.quizrc.exception;

import org.springframework.http.HttpStatus;

public abstract class HttpStatusException extends RuntimeException {
    public HttpStatusException(String message) {
        super(message);
    }

    public HttpStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract HttpStatus httpStatus();
}
