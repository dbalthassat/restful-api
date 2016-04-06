package com.dbalthassat.restapi.exception.clientError;

import com.dbalthassat.restapi.exception.ApiException;
import com.dbalthassat.restapi.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException(String message, Object... params) {
        super(String.format(message, params));
    }

    @Override
    public ExceptionCode code() {
        return ExceptionCode.NOT_FOUND;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
