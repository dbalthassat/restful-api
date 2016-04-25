package com.dbalthassat.restapi.exception.clientError.badRequest;

import com.dbalthassat.restapi.exception.ApiException;
import com.dbalthassat.restapi.exception.ExceptionValues;
import org.springframework.http.HttpStatus;

public abstract class AbstractBadRequestException extends ApiException {
    private final int code;

    public AbstractBadRequestException(ExceptionValues values, Object... params) {
        super(values.getMessage(), params);
        this.code = values.getCode();
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    public int code() {
        return code;
    }
}
