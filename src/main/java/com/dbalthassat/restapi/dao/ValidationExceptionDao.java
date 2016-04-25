package com.dbalthassat.restapi.dao;

import com.dbalthassat.restapi.exception.ExceptionValues;
import com.dbalthassat.restapi.exception.ValidationCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationExceptionDao extends ExceptionDao {
    private final List<ErrorDao> errors;

    public ValidationExceptionDao(List<FieldError> allErrors) {
        super(ExceptionValues.VALIDATION.getMessage(), ExceptionValues.VALIDATION.getCode(), HttpStatus.UNPROCESSABLE_ENTITY.value());
        errors = new LinkedList<>();
        errors.addAll(allErrors.stream()
                .map(ValidationExceptionDao::createErrorDao).collect(Collectors.toList()));
    }

    private static ErrorDao createErrorDao(FieldError error) {
        return new ErrorDao(ValidationCode.valueOfBySpringCode(error.getCode()).code(),
                            error.getField(),
                            error.getDefaultMessage()
        );
    }

    public List<ErrorDao> getErrors() {
        return errors;
    }

    private static class ErrorDao {
        private final int code;
        private final String field;
        private final String message;

        public ErrorDao(int code, String field, String message) {
            this.code = code;
            this.field = field;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}
