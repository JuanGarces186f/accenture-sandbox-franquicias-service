package com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.adapter;

import com.sandbox.accenture.franquicias_service.domain.model.Producto;
import com.sandbox.accenture.franquicias_service.domain.model.TopProductoPorSucursal;
import com.sandbox.accenture.franquicias_service.domain.port.out.ProductoRepositoryPort;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.mapper.ProductoEntityMapper;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.repository.ProductoJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductoRepositoryAdapter implements ProductoRepositoryPort {

    private final ProductoJpaRepository jpaRepository;
    private final ProductoEntityMapper mapper;

    @Autowired
    public ProductoRepositoryAdapter(ProductoJpaRepository jpaRepository,
                                     ProductoEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByNombreAndSucursalId(String nombre, Long sucursalId) {
        return jpaRepository.existsByNombreAndSucursalId(nombre, sucursalId);
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Producto save(Producto producto) {
        ProductoEntity entity;
        if (producto.getId() != null) {
            entity = jpaRepository.findById(producto.getId()).orElse(new ProductoEntity());
            entity.setNombre(producto.getNombre());
            entity.setStock(producto.getStock());
        } else {
            SucursalEntity sucursalRef = new SucursalEntity();
            sucursalRef.setId(producto.getSucursalId());
            entity = ProductoEntity.builder()
                    .nombre(producto.getNombre())
                    .stock(producto.getStock())
                    .sucursal(sucursalRef)
                    .build();
        }
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void delete(Producto producto) {
        jpaRepository.deleteById(producto.getId());
    }

    @Override
    public List<TopProductoPorSucursal> findTopStockProductosByFranquicia(Long franquiciaId) {
        return jpaRepository.findTopStockProductosByFranquicia(franquiciaId);
    }
}
