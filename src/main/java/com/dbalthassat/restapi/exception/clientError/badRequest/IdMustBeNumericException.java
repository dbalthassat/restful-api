package com.dbalthassat.restapi.exception.clientError.badRequest;

import com.dbalthassat.restapi.exception.ExceptionValues;

public final class IdMustBeNumericException extends AbstractBadRequestException {
    public IdMustBeNumericException() {
        super(ExceptionValues.ID_MUST_BE_NUMERIC);
    }
}
