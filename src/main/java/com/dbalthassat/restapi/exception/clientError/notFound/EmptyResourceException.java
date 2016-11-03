package com.dbalthassat.restapi.exception.clientError.notFound;

import com.dbalthassat.restapi.exception.ExceptionValues;

public final class EmptyResourceException extends AbstractNotFoundException {
    public EmptyResourceException() {
        super(ExceptionValues.EMPTY_RESOURCE);
    }
}
