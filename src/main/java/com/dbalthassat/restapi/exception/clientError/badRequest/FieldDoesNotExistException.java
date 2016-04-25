package com.dbalthassat.restapi.exception.clientError.badRequest;

import com.dbalthassat.restapi.exception.ExceptionValues;

public final class FieldDoesNotExistException extends AbstractBadRequestException {
    public FieldDoesNotExistException(String fieldName) {
        super(ExceptionValues.FIELD_DOES_NOT_EXIST, fieldName);
    }
}
