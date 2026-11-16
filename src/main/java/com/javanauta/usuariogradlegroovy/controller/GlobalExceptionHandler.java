package com.javanauta.usuariogradlegroovy.controller;


import com.javanauta.usuariogradlegroovy.infrastructure.exceptions.ConflictException;
import com.javanauta.usuariogradlegroovy.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.usuariogradlegroovy.infrastructure.exceptions.UnauthorizedExceptiom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@EnableGlobalAuthentication
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<String>("Error: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException(ConflictException ex) {
        return new ResponseEntity<String>("Conflict: " + ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedExceptiom.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedExceptiom ex) {
        return new ResponseEntity<String>("Unauthorized: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
