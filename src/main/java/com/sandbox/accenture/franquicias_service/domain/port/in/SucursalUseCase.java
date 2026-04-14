package com.sandbox.accenture.franquicias_service.domain.port.in;

import com.sandbox.accenture.franquicias_service.domain.model.Sucursal;

public interface SucursalUseCase {

    Sucursal agregarSucursal(Long franquiciaId, String nombre);

    Sucursal actualizarSucursal(Long sucursalId, String nuevoNombre);
}
