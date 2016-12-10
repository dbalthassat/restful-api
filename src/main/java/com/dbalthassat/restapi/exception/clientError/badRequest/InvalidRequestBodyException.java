package com.dbalthassat.restapi.exception.clientError.badRequest;

import com.dbalthassat.restapi.exception.ExceptionValues;

public class InvalidRequestBodyException extends AbstractBadRequestException {
	public InvalidRequestBodyException() {
		super(ExceptionValues.INVALID_REQUEST_BODY);
	}
}
