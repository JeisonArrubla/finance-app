package com.jeison.finance.finance_app.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullFieldException.class)
    public ResponseEntity<ErrorDetails> handleNullFieldException(NullFieldException e) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                "Datos del formulario proporcionados incompletos");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(errorDetails);
    }

    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<ErrorDetails> handleDuplicateFieldException(DuplicateFieldException e) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                "Error por campo duplicado en la base de datos");
        return ResponseEntity
                .status(HttpStatus.CONFLICT.value())
                .body(errorDetails);
    }
}
