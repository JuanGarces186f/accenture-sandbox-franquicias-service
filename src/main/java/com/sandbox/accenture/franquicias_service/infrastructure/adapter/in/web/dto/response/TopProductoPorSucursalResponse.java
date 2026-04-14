package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Producto con mayor stock por sucursal para una franquicia")
public record TopProductoPorSucursalResponse(

        @Schema(description = "ID de la sucursal", example = "1")
        Long sucursalId,

        @Schema(description = "Nombre de la sucursal", example = "Sucursal Norte")
        String sucursalNombre,

        @Schema(description = "ID del producto", example = "5")
        Long productoId,

        @Schema(description = "Nombre del producto con mayor stock", example = "Hamburguesa Doble")
        String productoNombre,

        @Schema(description = "Cantidad en stock", example = "150")
        Integer stock
) {}
