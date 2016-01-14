package com.dbalthassat.quizrc.exception;

import org.springframework.http.HttpStatus;

public class IllegalParameterException extends HttpStatusException {
    public IllegalParameterException(String message) {
        super(message);
    }

    public IllegalParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    HttpStatus httpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
