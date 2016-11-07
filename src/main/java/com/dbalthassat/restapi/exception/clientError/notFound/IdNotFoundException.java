package com.dbalthassat.restapi.exception.clientError.notFound;

import com.dbalthassat.restapi.exception.ExceptionValues;

public final class IdNotFoundException extends AbstractNotFoundException {
    public IdNotFoundException(String resource, Long id) {
        super(ExceptionValues.ID_NOT_FOUND, id, resource);
    }
}
