package com.dbalthassat.restapi.exception;

public class NotQueryableException extends BadRequestException {
    public NotQueryableException(String message) {
        super(message);
    }

    public NotQueryableException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int code() {
        return ExceptionCode.NOT_QUERYABLE.code();
    }
}
