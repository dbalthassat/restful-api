package com.dbalthassat.restapi.exception.clientError.notFound;

import com.dbalthassat.restapi.exception.ExceptionValues;

public final class RequestMethodNotFoundException extends AbstractNotFoundException {
    public RequestMethodNotFoundException(String method, String uri) {
        super(ExceptionValues.REQUEST_METHOD_NOT_FOUND, method, uri);
    }


}
