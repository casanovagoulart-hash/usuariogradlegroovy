package com.javanauta.usuariogradlegroovy.infrastructure.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedExceptiom extends AuthenticationException {

    public UnauthorizedExceptiom(String message) {
        super(message);
    }

    public UnauthorizedExceptiom(String message, Throwable cause) {
        super(message, cause);
    }


}
