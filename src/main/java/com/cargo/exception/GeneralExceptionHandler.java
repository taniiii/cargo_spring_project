package com.cargo.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

/**
 * Exception handler for base custom exception (CargoException).
 *
 * The exception contains HTTP status code and exception message.
 * The result is response entity with HTTP code and exception message.
 */
    @ExceptionHandler(CargoException.class)
    public ResponseEntity<Object> handleCargoException(CargoException e){
        String errorMessage = Objects.requireNonNull(e.getMessage(), e.getStatus().getReasonPhrase());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), e.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e){
        String errorMessage = e.getConstraintViolations().stream()
                .filter(Objects::nonNull)
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElseGet(BAD_REQUEST::getReasonPhrase);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e){
        String errorMessage = Objects.requireNonNull(e.getMessage(), INTERNAL_SERVER_ERROR.getReasonPhrase());
        return new ResponseEntity<>(errorMessage,
                new HttpHeaders(), INTERNAL_SERVER_ERROR);
    }

}
