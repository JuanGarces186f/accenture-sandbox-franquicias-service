package com.sandbox.accenture.franquicias_service.domain.port.in;

import com.sandbox.accenture.franquicias_service.domain.model.Producto;
import com.sandbox.accenture.franquicias_service.domain.model.TopProductoPorSucursal;

import java.util.List;

public interface ProductoUseCase {

    Producto agregarProducto(Long sucursalId, String nombre, Integer stock);

    void eliminarProducto(Long productoId);

    Producto actualizarStock(Long productoId, Integer nuevoStock);

    Producto actualizarNombre(Long productoId, String nuevoNombre);

    Producto actualizarProducto(Long productoId, String nuevoNombre, Integer nuevoStock);

    List<TopProductoPorSucursal> obtenerTopProductosPorFranquicia(Long franquiciaId);
}
