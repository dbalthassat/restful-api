package com.dbalthassat.restapi.exception.clientError.badRequest;

import com.dbalthassat.restapi.exception.ExceptionValues;

public final class RequestBodyMissingException extends AbstractBadRequestException {
    public RequestBodyMissingException() {
        super(ExceptionValues.REQUEST_BODY_IS_MISSING);
    }
}
