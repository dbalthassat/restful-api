package com.dbalthassat.restapi.exception.clientError.notFound;

import com.dbalthassat.restapi.exception.ApiException;
import com.dbalthassat.restapi.exception.ExceptionValues;
import org.springframework.http.HttpStatus;

public class AbstractNotFoundException extends ApiException {
    private final int code;

    public AbstractNotFoundException(ExceptionValues values, Object... params) {
        super(values.getMessage(), params);
        this.code = values.getCode();
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }

    public int code() {
        return code;
    }
}
