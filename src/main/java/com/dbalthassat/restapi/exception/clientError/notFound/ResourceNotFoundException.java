package com.dbalthassat.restapi.exception.clientError.notFound;

import com.dbalthassat.restapi.exception.ExceptionValues;

public final class ResourceNotFoundException extends AbstractNotFoundException {
    public ResourceNotFoundException(String uri) {
        super(ExceptionValues.RESOURCE_NOT_FOUND, uri);
    }
}
