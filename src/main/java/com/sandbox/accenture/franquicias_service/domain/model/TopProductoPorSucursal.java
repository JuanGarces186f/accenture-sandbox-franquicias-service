package com.sandbox.accenture.franquicias_service.domain.model;

public record TopProductoPorSucursal(
        Long sucursalId,
        String sucursalNombre,
        Long productoId,
        String productoNombre,
        Integer stock
) {}
