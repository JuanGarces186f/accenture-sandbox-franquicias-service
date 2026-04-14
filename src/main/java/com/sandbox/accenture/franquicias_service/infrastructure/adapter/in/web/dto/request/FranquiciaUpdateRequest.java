package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para actualización parcial de una franquicia. Solo envía los campos a modificar, los null se ignoran")
public class FranquiciaUpdateRequest {

    @Size(min = 1, message = "El nombre no puede ser vacío")
    @Schema(description = "Nuevo nombre de la franquicia", example = "Burger King", nullable = true)
    private String nombre;
}
