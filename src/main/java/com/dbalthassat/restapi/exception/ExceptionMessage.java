package com.dbalthassat.restapi.exception;

public enum ExceptionMessage {
    GREETINGS_NOT_FOUND("Greeting %d not found.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
