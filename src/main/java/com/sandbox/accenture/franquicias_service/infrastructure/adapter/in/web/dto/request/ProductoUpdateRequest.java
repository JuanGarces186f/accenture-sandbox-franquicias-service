package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para actualización parcial de un producto. Solo envía los campos a modificar, los null se ignoran")
public class ProductoUpdateRequest {

    @Size(min = 1, message = "El nombre no puede ser vacío")
    @Schema(description = "Nuevo nombre del producto", example = "Hamburguesa Triple", nullable = true)
    private String nombre;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(description = "Nuevo stock del producto", example = "200", nullable = true)
    private Integer stock;
}
