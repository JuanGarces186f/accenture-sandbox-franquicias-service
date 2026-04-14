package com.sandbox.accenture.franquicias_service.domain.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String resourceName, String fieldValue) {
        super(String.format("%s con el nombre '%s' ya existe", resourceName, fieldValue));
    }
}
