package com.sandbox.accenture.franquicias_service.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND"),
    DUPLICATE_RESOURCE("DUPLICATE_RESOURCE"),
    VALIDATION_ERROR("VALIDATION_ERROR"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR");

    private final String code;
}
