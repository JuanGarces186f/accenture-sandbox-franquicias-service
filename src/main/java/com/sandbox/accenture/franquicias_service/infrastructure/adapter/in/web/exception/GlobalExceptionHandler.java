package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.exception;

import com.sandbox.accenture.franquicias_service.domain.exception.DuplicateResourceException;
import com.sandbox.accenture.franquicias_service.domain.exception.ErrorCode;
import com.sandbox.accenture.franquicias_service.domain.exception.ResourceNotFoundException;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ErrorApiResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private String generateTraceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private ErrorResponse buildError(HttpStatus status, ErrorCode code, String message,
                                     List<String> details, String traceId) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(status.value());
        error.setCode(code.getCode());
        error.setMessage(message);
        error.setDetails(details);
        error.setTraceId(traceId);
        return error;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorApiResponse> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.warn("[{}] Recurso no encontrado: {}", traceId, ex.getMessage());
        ErrorResponse error = buildError(HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND,
                ex.getMessage(), null, traceId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorApiResponse.of(error));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorApiResponse> handleDuplicateResource(
            DuplicateResourceException ex, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.warn("[{}] Recurso duplicado: {}", traceId, ex.getMessage());
        ErrorResponse error = buildError(HttpStatus.CONFLICT, ErrorCode.DUPLICATE_RESOURCE,
                ex.getMessage(), null, traceId);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorApiResponse.of(error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String traceId = generateTraceId();
        List<String> details = ex.getBindingResult().getAllErrors().stream()
                .map(err -> ((FieldError) err).getField() + ": " + err.getDefaultMessage())
                .toList();
        log.warn("[{}] Errores de validación: {}", traceId, details);
        ErrorResponse error = buildError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR,
                "Error de validación en los datos enviados", details, traceId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorApiResponse.of(error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApiResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.error("[{}] Error inesperado en {}: {}", traceId, request.getRequestURI(), ex.getMessage(), ex);
        ErrorResponse error = buildError(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR,
                "Ha ocurrido un error inesperado en el servidor", null, traceId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorApiResponse.of(error));
    }
}
