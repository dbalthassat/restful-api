package com.dbalthassat.restapi.dao;

import com.dbalthassat.restapi.exception.ExceptionValues;
import com.dbalthassat.restapi.exception.ValidationCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class ValidationExceptionDao extends ExceptionDao {
    private List<ErrorDao> errors;

    /**
     * Pour les tests uniquement.
     */
    public ValidationExceptionDao() {
        this(Collections.emptyList());
    }

    public ValidationExceptionDao(List<FieldError> allErrors) {
        super(ExceptionValues.VALIDATION.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value(), ExceptionValues.VALIDATION.getCode());
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

    public void setErrors(List<ErrorDao> errors) {
        this.errors = errors;
    }

    private static class ErrorDao {
        private int code;
        private String field;
        private String message;

        /**
         * Pour les tests uniquement.
         */
        public ErrorDao() {
        }

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

        public void setCode(int code) {
            this.code = code;
        }

        public void setField(String field) {
            this.field = field;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
