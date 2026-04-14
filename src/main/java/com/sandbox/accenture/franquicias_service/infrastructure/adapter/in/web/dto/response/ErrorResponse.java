package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Detalle del error cuando la operación falla")
public class ErrorResponse {

    @Schema(description = "Código HTTP del error", example = "404")
    private int status;

    @Schema(description = "Código interno del error", example = "RESOURCE_NOT_FOUND")
    private String code;

    @Schema(description = "Mensaje descriptivo del error")
    private String message;

    @Schema(description = "Lista de detalles adicionales del error (ej: errores de validación por campo)")
    private List<String> details;

    @Schema(description = "Timestamp Unix en milisegundos", example = "1773934758921")
    private long timestamp = System.currentTimeMillis();

    @Schema(description = "ID de trazabilidad de la request para correlación en logs", example = "abc123-xyz")
    private String traceId;
}
