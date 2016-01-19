package com.dbalthassat.quizrc.exception;

public class NotQueryableException extends BadRequestException {
    public NotQueryableException(String message) {
        super(message);
    }

    public NotQueryableException(String message, Throwable cause) {
        super(message, cause);
    }
}
