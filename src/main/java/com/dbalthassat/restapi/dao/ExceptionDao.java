package com.dbalthassat.restapi.dao;

import com.dbalthassat.restapi.exception.ApiException;
import com.dbalthassat.restapi.exception.ExceptionCode;

public class ExceptionDao {
    private final String message;
    private final int status;
    private final int code;

    public ExceptionDao(ApiException exception) {
        this.message = exception.getMessage();
        this.status = exception.status().value();
        this.code = exception.code().code();
    }

    public ExceptionDao(Throwable exception) {
        this(exception.getMessage());
    }

    public ExceptionDao(String message) {
        this(message, ExceptionCode.BAD_REQUEST.code());
    }

    public ExceptionDao(String message, int status) {
        this(message, status, status);
    }

    public ExceptionDao(String message, int status, int code) {
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
