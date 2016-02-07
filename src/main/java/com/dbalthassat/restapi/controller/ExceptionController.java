package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.entity.ExceptionEntity;
import com.dbalthassat.restapi.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice(annotations = RestController.class)
public class ExceptionController {
    Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ExceptionEntity> exceptionHandler(ApiException e) {
		LOGGER.info(String.format("The following error occured: %s", e.getMessage()));
		return new ResponseEntity<>(new ExceptionEntity(e), e.status());
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<ExceptionEntity> exceptionHandler(Throwable e) {
		LOGGER.error(e.getMessage(), e);
		return new ResponseEntity<>(new ExceptionEntity(e), HttpStatus.BAD_REQUEST);
	}
}
