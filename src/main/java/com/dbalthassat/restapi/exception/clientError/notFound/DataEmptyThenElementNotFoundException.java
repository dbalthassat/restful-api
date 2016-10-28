package com.dbalthassat.restapi.exception.clientError.notFound;

import com.dbalthassat.restapi.exception.ExceptionValues;

public final class DataEmptyThenElementNotFoundException extends AbstractNotFoundException {
    public DataEmptyThenElementNotFoundException(String resource) {
        super(ExceptionValues.DATA_EMPTY_THEN_ELEMENT_NOT_FOUND, resource);
    }
}