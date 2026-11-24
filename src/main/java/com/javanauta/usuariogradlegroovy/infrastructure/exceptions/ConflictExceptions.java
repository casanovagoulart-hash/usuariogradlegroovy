package com.javanauta.usuariogradlegroovy.infrastructure.exceptions;

public class ConflictExceptions extends RuntimeException {

    // Construtor simples
    public ConflictExceptions(String message) {
        super(message);
    }

    // Construtor com causa (útil para rastrear erros)
    public ConflictExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    // Método de fábrica estático
    public static ConflictExceptions createConflictException(String message) {
        return new ConflictExceptions(message);
    }
}
