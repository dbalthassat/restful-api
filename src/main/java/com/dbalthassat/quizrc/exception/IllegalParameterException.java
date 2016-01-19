package com.dbalthassat.quizrc.exception;

public class IllegalParameterException extends BadRequestException {
    public IllegalParameterException(String message) {
        super(message);
    }

    public IllegalParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
