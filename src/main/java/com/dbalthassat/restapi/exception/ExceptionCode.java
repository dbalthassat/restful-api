package com.dbalthassat.restapi.exception;

public enum ExceptionCode {
	BAD_REQUEST(400),
	ILLEGAL_PARAMETER(40001),
	NOT_QUERYABLE(40002),

	NOT_FOUND(404),
	RESOURCE_NOT_FOUND(40401);

	private final int code;

	ExceptionCode(int code) {
		this.code = code;
	}

	public int code() {
		return code;
	}
}
