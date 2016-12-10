package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.dao.exception.ExceptionDao;
import com.dbalthassat.restapi.dao.exception.ValidationExceptionDao;
import com.dbalthassat.restapi.exception.ApiException;
import com.dbalthassat.restapi.exception.clientError.badRequest.RequestBodyMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;


@ControllerAdvice(annotations = RestController.class)
public class ExceptionController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ExceptionDao> exceptionHandler(ApiException exception) {
		LOGGER.debug(String.format("The following error occured: %s", exception.getMessage()));
		return new ResponseEntity<>(new ExceptionDao(exception), exception.status());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationExceptionDao> exceptionHandler(MethodArgumentNotValidException exception) {
		LOGGER.debug("Some validation errors occured.");
		ValidationExceptionDao body = new ValidationExceptionDao(exception.getBindingResult().getFieldErrors());
		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionDao> exceptionHandler() {
		return exceptionHandler(new RequestBodyMissingException());
	}

	// TODO externaliser les messages
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<ExceptionDao> exceptionHandler(Throwable exception) {
		int code = Integer.parseInt("500" + Integer.toString(Math.abs(new Random().nextInt() % 10000)));
		LOGGER.error("Error " + code + ": " + exception.getMessage(), exception);
		return new ResponseEntity<>(new ExceptionDao("Une erreur technique est survenue. Merci de contacter l'administrateur du site avec le code d'erreur suivant : " + code, code), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
