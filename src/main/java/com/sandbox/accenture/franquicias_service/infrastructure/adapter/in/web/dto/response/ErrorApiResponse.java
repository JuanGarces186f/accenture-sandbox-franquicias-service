package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Envelope estándar de la API para respuestas de error")
public class ErrorApiResponse {

    @Schema(description = "Indica si la operación fue exitosa", example = "false")
    private boolean success = false;

    @Schema(description = "Mensaje descriptivo del error")
    private String message;

    @Schema(description = "Detalle estructurado del error")
    private ErrorResponse error;

    @Schema(description = "Timestamp Unix en milisegundos", example = "1773934758921")
    private long timestamp = System.currentTimeMillis();

    public static ErrorApiResponse of(ErrorResponse error) {
        ErrorApiResponse response = new ErrorApiResponse();
        response.setError(error);
        response.setMessage(error != null ? error.getMessage() : null);
        return response;
    }
}
