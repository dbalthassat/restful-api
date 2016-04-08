package com.dbalthassat.restapi.exception;

import java.util.Arrays;
import java.util.Optional;

public enum ValidationCode {
    DEFAULT("Default", 4220100),
    NOT_NULL("NotNull", 4220101);

    private final String springCode;
    private final int code;

    ValidationCode(String springCode, int code) {
        this.springCode = springCode;
        this.code = code;
    }

    public int code() {
        return code;
    }

    public String getSpringCode() {
        return springCode;
    }

    public static ValidationCode valueOfBySpringCode(String code) {
        Optional<ValidationCode> op = Arrays.stream(values()).filter(e -> e.getSpringCode().equals(code)).findAny();
        return op.orElse(DEFAULT);
    }
}
