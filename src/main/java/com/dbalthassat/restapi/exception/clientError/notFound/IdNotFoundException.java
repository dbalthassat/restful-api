package com.dbalthassat.restapi.exception.clientError.notFound;

import com.dbalthassat.restapi.exception.ExceptionValues;

public final class IdNotFoundException extends AbstractNotFoundException {
    public IdNotFoundException(String uri) {
        super(ExceptionValues.ID_NOT_FOUND, uri);
    }
}
