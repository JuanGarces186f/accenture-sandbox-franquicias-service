package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para agregar un nuevo producto a una sucursal")
public class ProductoRequest {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 1, message = "El nombre no puede ser vacío")
    @Schema(description = "Nombre del producto", example = "Hamburguesa Doble")
    private String nombre;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(description = "Cantidad inicial en stock", example = "150")
    private Integer stock;
}
