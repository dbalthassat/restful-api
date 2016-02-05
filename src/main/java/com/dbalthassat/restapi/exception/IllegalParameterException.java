package com.dbalthassat.restapi.exception;

public class IllegalParameterException extends BadRequestException {
    public IllegalParameterException(String message, Object... params) {
        super(String.format(message, params));
    }
}
