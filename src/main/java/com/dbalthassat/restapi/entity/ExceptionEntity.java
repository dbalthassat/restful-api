package com.dbalthassat.restapi.entity;

import com.dbalthassat.restapi.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ExceptionEntity {
    private Integer status;
    private Integer code;
    private String message;

    public ExceptionEntity(ApiException exception) {
        this.status = exception.status().value();
        this.code = exception.code();
        this.message = exception.getMessage();
    }

    public ExceptionEntity(Throwable exception) {
        this.status = HttpStatus.BAD_REQUEST.value();
        this.code = HttpStatus.BAD_REQUEST.value();
        this.message = exception.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
