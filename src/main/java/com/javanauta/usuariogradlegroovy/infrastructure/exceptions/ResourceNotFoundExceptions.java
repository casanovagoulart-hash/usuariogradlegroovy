package com.javanauta.usuariogradlegroovy.infrastructure.exceptions;

public class ResourceNotFoundExceptions extends RuntimeException{
    public ResourceNotFoundExceptions(String mensage){
        super(mensage);
    }
    public ResourceNotFoundExceptions(String mensage, Throwable Throwable){
        super(mensage, Throwable);
    }

}
