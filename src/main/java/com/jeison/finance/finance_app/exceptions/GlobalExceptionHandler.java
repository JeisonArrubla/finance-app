package com.jeison.finance.finance_app.exceptions;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler({ NullFieldException.class,
                        IllegalArgumentException.class })
        public ResponseEntity<ErrorDetails> handleNullFieldException(Exception e) {
                ErrorDetails errorDetails = new ErrorDetails(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                e.getMessage());
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST.value())
                                .body(errorDetails);
        }

        @ExceptionHandler(DuplicateKeyException.class)
        public ResponseEntity<ErrorDetails> handleDuplicateFieldException(DuplicateKeyException e) {
                ErrorDetails errorDetails = new ErrorDetails(
                                LocalDateTime.now(),
                                HttpStatus.CONFLICT.value(),
                                e.getMessage());
                return ResponseEntity
                                .status(HttpStatus.CONFLICT.value())
                                .body(errorDetails);
        }

        @ExceptionHandler({ NullPointerException.class,
                        ResourceNotFoundException.class,
                        EntityNotFoundException.class,
                        NoSuchElementException.class })
        public ResponseEntity<ErrorDetails> handleNotFoundException(Exception e) {
                ErrorDetails errorDetails = new ErrorDetails(
                                LocalDateTime.now(),
                                HttpStatus.NOT_FOUND.value(),
                                e.getMessage());
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(errorDetails);
        }
}
