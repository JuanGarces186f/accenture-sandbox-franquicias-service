package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Información básica de una franquicia sin sucursales ni productos")
public class FranquiciaSimpleResponse {

    @Schema(description = "ID de la franquicia", example = "1")
    private Long id;

    @Schema(description = "Nombre de la franquicia", example = "McDonald's")
    private String nombre;
}
