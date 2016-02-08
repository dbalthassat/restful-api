package com.dbalthassat.restapi.entity;

import com.dbalthassat.restapi.exception.ApiException;
import com.dbalthassat.restapi.exception.ExceptionCode;

public class ExceptionEntity {
    private final String message;
    private final int status;
    private final int code;

    public ExceptionEntity(ApiException exception) {
        this.message = exception.getMessage();
        this.status = exception.status().value();
        this.code = exception.code();
    }

    public ExceptionEntity(Throwable exception) {
        this(exception.getMessage());
    }

    public ExceptionEntity(String message) {
        this(message, ExceptionCode.BAD_REQUEST.code());
    }

    public ExceptionEntity(String message, int status) {
        this(message, status, status);
    }

    public ExceptionEntity(String message, int status, int code) {
        this.message = message;
        this.status = status;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }
}
