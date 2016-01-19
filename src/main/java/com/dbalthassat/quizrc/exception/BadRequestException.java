package com.dbalthassat.quizrc.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpStatusException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
