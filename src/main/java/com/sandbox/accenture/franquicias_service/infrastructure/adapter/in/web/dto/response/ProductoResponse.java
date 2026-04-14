package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Datos de un producto")
public class ProductoResponse {

    @Schema(description = "ID del producto", example = "1")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Hamburguesa Doble")
    private String nombre;

    @Schema(description = "Cantidad en stock", example = "150")
    private Integer stock;
}
