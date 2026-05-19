package com.svault.vault.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Object> handleHttpException(HttpException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ex.getData() != null ? ex.getData() : ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    @ExceptionHandler(TooManyAttemptsException.class)
    public ResponseEntity<?> handleTooMany(TooManyAttemptsException ex) {
        return ResponseEntity.status(429)
                .body(Map.of("message", ex.getMessage()));
    }
}