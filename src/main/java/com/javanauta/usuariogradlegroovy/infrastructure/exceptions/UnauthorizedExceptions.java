package com.javanauta.usuariogradlegroovy.infrastructure.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedExceptions extends AuthenticationException {

    public UnauthorizedExceptions(String message) {
        super(message);
    }

    public UnauthorizedExceptions(String message, Throwable cause) {
        super(message, cause);
    }


}
