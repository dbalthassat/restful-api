package com.dbalthassat.restapi.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionCode {
	BAD_REQUEST(400),
	ILLEGAL_PARAMETER(40001),
	NOT_QUERYABLE(40002),
	NOT_FOUND(404);

	private final int code;

	ExceptionCode(int code) {
		this.code = code;
	}

	public int code() {
		return code;
	}
}
