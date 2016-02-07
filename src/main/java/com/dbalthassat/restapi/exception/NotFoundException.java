package com.dbalthassat.restapi.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException(String message, Object... params) {
        super(String.format(message, params));
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException() {
        this("Item not found");
    }

    @Override
    public int code() {
        return ExceptionCode.NOT_FOUND.code();
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
