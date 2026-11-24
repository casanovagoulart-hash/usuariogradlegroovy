package com.javanauta.usuariogradlegroovy.controller;

import com.javanauta.usuariogradlegroovy.infrastructure.exceptions.ConflictExceptions;
import com.javanauta.usuariogradlegroovy.infrastructure.exceptions.ResourceNotFoundExceptions;
import com.javanauta.usuariogradlegroovy.infrastructure.exceptions.UnauthorizedExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// CORREÇÃO: Removido @EnableGlobalAuthentication e o import não utilizado do Spring Security
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundExceptions.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundExceptions ex) {
        return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictExceptions.class)
    public ResponseEntity<String> handleConflictException(ConflictExceptions ex) {
        return new ResponseEntity<>("Conflict: " + ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedExceptions.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedExceptions ex) {
        return new ResponseEntity<>("Unauthorized: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
