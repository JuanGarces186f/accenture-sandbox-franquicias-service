package com.sandbox.accenture.franquicias_service.domain.port.out;

import com.sandbox.accenture.franquicias_service.domain.model.Producto;
import com.sandbox.accenture.franquicias_service.domain.model.TopProductoPorSucursal;

import java.util.List;
import java.util.Optional;

public interface ProductoRepositoryPort {

    boolean existsByNombreAndSucursalId(String nombre, Long sucursalId);

    Optional<Producto> findById(Long id);

    Producto save(Producto producto);

    void delete(Producto producto);

    List<TopProductoPorSucursal> findTopStockProductosByFranquicia(Long franquiciaId);
}
