package com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.adapter;

import com.sandbox.accenture.franquicias_service.domain.model.Franquicia;
import com.sandbox.accenture.franquicias_service.domain.port.out.FranquiciaRepositoryPort;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.mapper.FranquiciaEntityMapper;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.repository.FranquiciaJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FranquiciaRepositoryAdapter implements FranquiciaRepositoryPort {

    private final FranquiciaJpaRepository jpaRepository;
    private final FranquiciaEntityMapper mapper;

    @Autowired
    public FranquiciaRepositoryAdapter(FranquiciaJpaRepository jpaRepository,
                                       FranquiciaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return jpaRepository.existsByNombre(nombre);
    }

    @Override
    public Optional<Franquicia> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Franquicia> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Franquicia save(Franquicia franquicia) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(franquicia)));
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
