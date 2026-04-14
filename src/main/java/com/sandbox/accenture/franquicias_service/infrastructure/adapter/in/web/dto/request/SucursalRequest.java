package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para crear una nueva sucursal")
public class SucursalRequest {

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Size(min = 1, message = "El nombre no puede ser vacío")
    @Schema(description = "Nombre de la sucursal", example = "Sucursal Norte")
    private String nombre;
}
