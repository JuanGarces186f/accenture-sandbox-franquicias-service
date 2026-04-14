package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Datos de una sucursal con sus productos")
public class SucursalResponse {

    @Schema(description = "ID de la sucursal", example = "1")
    private Long id;

    @Schema(description = "Nombre de la sucursal", example = "Sucursal Norte")
    private String nombre;

    @Schema(description = "Lista de productos ofertados en la sucursal")
    private List<ProductoResponse> productos;
}
