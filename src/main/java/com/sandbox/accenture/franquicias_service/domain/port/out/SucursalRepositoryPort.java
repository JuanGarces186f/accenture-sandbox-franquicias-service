package com.sandbox.accenture.franquicias_service.domain.port.out;

import com.sandbox.accenture.franquicias_service.domain.model.Sucursal;

import java.util.Optional;

public interface SucursalRepositoryPort {

    boolean existsByNombreAndFranquiciaId(String nombre, Long franquiciaId);

    Optional<Sucursal> findById(Long id);

    Sucursal save(Sucursal sucursal);
}
