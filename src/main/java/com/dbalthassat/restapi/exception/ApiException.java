package com.dbalthassat.restapi.exception;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
	public ApiException(String message, Object... params) {
		super(String.format(message, params));
	}

	public abstract int code();

	public abstract HttpStatus status();
}
