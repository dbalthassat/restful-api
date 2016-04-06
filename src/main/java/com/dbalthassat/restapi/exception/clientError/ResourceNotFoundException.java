package com.dbalthassat.restapi.exception.clientError;

import com.dbalthassat.restapi.exception.ExceptionCode;

public class ResourceNotFoundException extends NotFoundException {
    public ResourceNotFoundException(String message, Object... params) {
        super(message, params);
    }

    @Override
    public ExceptionCode code() {
        return ExceptionCode.RESOURCE_NOT_FOUND;
    }
}
