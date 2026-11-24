package com.javanauta.usuariogradlegroovy.infrastructure.exceptions;

public class IllegalArgumentExceptions extends RuntimeException {
    public IllegalArgumentExceptions(String message) {
        super(message);
    }

public IllegalArgumentExceptions(String message, Throwable throwable) {
        super(message, throwable);
}

}
