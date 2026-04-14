package com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.adapter;

import com.sandbox.accenture.franquicias_service.domain.model.Sucursal;
import com.sandbox.accenture.franquicias_service.domain.port.out.SucursalRepositoryPort;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.entity.FranquiciaEntity;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.mapper.SucursalEntityMapper;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.repository.SucursalJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SucursalRepositoryAdapter implements SucursalRepositoryPort {

    private final SucursalJpaRepository jpaRepository;
    private final SucursalEntityMapper mapper;

    @Autowired
    public SucursalRepositoryAdapter(SucursalJpaRepository jpaRepository,
                                     SucursalEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    @Override
    public boolean existsByNombreAndFranquiciaId(String nombre, Long franquiciaId) {
        return jpaRepository.existsByNombreAndFranquiciaId(nombre, franquiciaId);
    }

    @Override
    public Optional<Sucursal> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Sucursal save(Sucursal sucursal) {
        SucursalEntity entity;
        if (sucursal.getId() != null) {
            entity = jpaRepository.findById(sucursal.getId()).orElse(new SucursalEntity());
            entity.setNombre(sucursal.getNombre());
        } else {
            FranquiciaEntity franquiciaRef = new FranquiciaEntity();
            franquiciaRef.setId(sucursal.getFranquiciaId());
            entity = SucursalEntity.builder()
                    .nombre(sucursal.getNombre())
                    .franquicia(franquiciaRef)
                    .build();
        }
        return mapper.toDomain(jpaRepository.save(entity));
    }
}
