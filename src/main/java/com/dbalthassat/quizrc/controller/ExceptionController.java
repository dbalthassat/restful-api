package com.dbalthassat.quizrc.controller;

import com.dbalthassat.quizrc.entity.ExceptionEntity;
import com.dbalthassat.quizrc.exception.HttpStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
// TODO vérifier que cette implémentation est RESTful compliant.
public interface ExceptionController {
    Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(HttpStatusException.class)
    default ExceptionEntity exceptionHandler(HttpStatusException e) {
        LOGGER.info(String.format("The following error occured: %s", e.getMessage()), e);
        return new ExceptionEntity(e.getMessage(), e.httpStatus().value());
    }

    @ExceptionHandler(Throwable.class)
    default ExceptionEntity exceptionHandler(Throwable e) {
        LOGGER.warn(e.getMessage(), e);
        return new ExceptionEntity(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
}
