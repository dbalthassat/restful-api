package com.dbalthassat.restapi.exception.serverError;

public class TechnicalException extends RuntimeException {
	public TechnicalException(Throwable t) {
		super(t);
	}
}
