package com.github.ttrojan.app;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value
            = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleException(
            RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

}
