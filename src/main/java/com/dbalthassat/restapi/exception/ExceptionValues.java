package com.dbalthassat.restapi.exception;

import org.springframework.http.MediaType;

public enum ExceptionValues {
	FIELD_DOES_NOT_EXIST("The field '%s' does not exist.", 40001),
	REQUEST_BODY_IS_MISSING("Required request body is missing.", 40002),
	WRONG_CONTENT_TYPE(String.format("The request should contains header Content-Type with value %s.", MediaType.APPLICATION_JSON_VALUE), 40003),
	ID_MUST_BE_NUMERIC("THe id must be numeric.", 40005),

	RESOURCE_NOT_FOUND("Request method %s not supported for path %s.", 40401),
	ID_NOT_FOUND("Resource %d not found.", 40402),

	VALIDATION("Validation failed.", 42201);

	private final String message;
	private final int code;

	ExceptionValues(String message, int code) {
		this.message = message;
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
