package com.dbalthassat.restapi.dao.exception;

import com.dbalthassat.restapi.exception.ApiException;

@SuppressWarnings("unused")
public class ExceptionDao {
    private String message;
    private int status;
    private int code;

    /**
     * Pour les tests uniquement.
     */
    public ExceptionDao() {
    }

    public ExceptionDao(ApiException exception) {
        this.message = exception.getMessage();
        this.status = exception.status().value();
        this.code = exception.code();
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

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
