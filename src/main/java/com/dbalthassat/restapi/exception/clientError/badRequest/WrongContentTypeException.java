package com.dbalthassat.restapi.exception.clientError.badRequest;

import com.dbalthassat.restapi.exception.ExceptionValues;

public final class WrongContentTypeException extends AbstractBadRequestException {
    public WrongContentTypeException() {
        super(ExceptionValues.WRONG_CONTENT_TYPE);
    }
}
