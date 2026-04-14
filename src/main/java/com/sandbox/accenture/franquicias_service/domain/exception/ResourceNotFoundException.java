package com.sandbox.accenture.franquicias_service.domain.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s con id '%d' no fue encontrado", resourceName, id));
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
