package com.dbalthassat.restapi.exception;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
	public ApiException() {
	}

	public ApiException(String message, Object... params) {
		super(String.format(message, params));
	}

	public ApiException(String message, Throwable cause, Object... params) {
		super(String.format(message, params), cause);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

	public abstract int code();

	public abstract HttpStatus status();
}
