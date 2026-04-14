package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Datos completos de una franquicia con sus sucursales y productos")
public class FranquiciaResponse {

    @Schema(description = "ID de la franquicia", example = "1")
    private Long id;

    @Schema(description = "Nombre de la franquicia", example = "McDonald's")
    private String nombre;

    @Schema(description = "Lista de sucursales de la franquicia")
    private List<SucursalResponse> sucursales;
}
