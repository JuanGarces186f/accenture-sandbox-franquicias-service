package com.sandbox.accenture.franquicias_service.application.usecase;

import com.sandbox.accenture.franquicias_service.domain.exception.DuplicateResourceException;
import com.sandbox.accenture.franquicias_service.domain.exception.ResourceNotFoundException;
import com.sandbox.accenture.franquicias_service.domain.model.Producto;
import com.sandbox.accenture.franquicias_service.domain.model.Sucursal;
import com.sandbox.accenture.franquicias_service.domain.model.TopProductoPorSucursal;
import com.sandbox.accenture.franquicias_service.domain.port.in.ProductoUseCase;
import com.sandbox.accenture.franquicias_service.domain.port.out.FranquiciaRepositoryPort;
import com.sandbox.accenture.franquicias_service.domain.port.out.ProductoRepositoryPort;
import com.sandbox.accenture.franquicias_service.domain.port.out.SucursalRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ProductoUseCaseImpl implements ProductoUseCase {

    private final SucursalRepositoryPort sucursalRepositoryPort;
    private final ProductoRepositoryPort productoRepositoryPort;
    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    @Autowired
    public ProductoUseCaseImpl(SucursalRepositoryPort sucursalRepositoryPort,
                               ProductoRepositoryPort productoRepositoryPort,
                               FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.sucursalRepositoryPort = sucursalRepositoryPort;
        this.productoRepositoryPort = productoRepositoryPort;
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    @Override
    @Transactional
    public Producto agregarProducto(Long sucursalId, String nombre, Integer stock) {
        log.debug("Agregando producto '{}' a sucursal id: {}", nombre, sucursalId);

        Sucursal sucursal = sucursalRepositoryPort.findById(sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", sucursalId));

        // Validación de duplicado en el modelo de dominio, sin consulta adicional a la BD
        if (sucursal.tieneProductoConNombre(nombre)) {
            throw new DuplicateResourceException("Producto", nombre);
        }

        Producto producto = Producto.builder().nombre(nombre).stock(stock).sucursalId(sucursalId).build();
        Producto saved = productoRepositoryPort.save(producto);
        log.info("Producto creado con id: {} en sucursal id: {}", saved.getId(), sucursalId);
        return saved;
    }

    @Override
    @Transactional
    public void eliminarProducto(Long productoId) {
        log.debug("Eliminando producto id: {}", productoId);
        Producto producto = productoRepositoryPort.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", productoId));
        productoRepositoryPort.delete(producto);
        log.info("Producto id: {} eliminado", productoId);
    }

    @Override
    @Transactional
    public Producto actualizarStock(Long productoId, Integer nuevoStock) {
        Producto producto = productoRepositoryPort.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", productoId));
        producto.setStock(nuevoStock);
        return productoRepositoryPort.save(producto);
    }

    @Override
    @Transactional
    public Producto actualizarNombre(Long productoId, String nuevoNombre) {
        Producto producto = productoRepositoryPort.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", productoId));
        if (productoRepositoryPort.existsByNombreAndSucursalId(nuevoNombre, producto.getSucursalId())
                && !producto.getNombre().equals(nuevoNombre)) {
            throw new DuplicateResourceException("Producto", nuevoNombre);
        }
        producto.setNombre(nuevoNombre);
        return productoRepositoryPort.save(producto);
    }

    @Override
    @Transactional
    public Producto actualizarProducto(Long productoId, String nuevoNombre, Integer nuevoStock) {
        log.debug("Actualizando producto id: {}", productoId);
        Producto producto = productoRepositoryPort.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", productoId));
        if (nuevoNombre != null) {
            if (productoRepositoryPort.existsByNombreAndSucursalId(nuevoNombre, producto.getSucursalId())
                    && !producto.getNombre().equals(nuevoNombre)) {
                throw new DuplicateResourceException("Producto", nuevoNombre);
            }
            producto.setNombre(nuevoNombre);
        }
        if (nuevoStock != null) {
            producto.setStock(nuevoStock);
        }
        Producto updated = productoRepositoryPort.save(producto);
        log.info("Producto id: {} actualizado", productoId);
        return updated;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopProductoPorSucursal> obtenerTopProductosPorFranquicia(Long franquiciaId) {
        log.debug("Consultando top productos para franquicia id: {}", franquiciaId);
        if (!franquiciaRepositoryPort.existsById(franquiciaId)) {
            throw new ResourceNotFoundException("Franquicia", franquiciaId);
        }
        List<TopProductoPorSucursal> result = productoRepositoryPort.findTopStockProductosByFranquicia(franquiciaId);
        log.info("Se encontraron {} sucursales con top productos para franquicia id: {}", result.size(), franquiciaId);
        return result;
    }
}
