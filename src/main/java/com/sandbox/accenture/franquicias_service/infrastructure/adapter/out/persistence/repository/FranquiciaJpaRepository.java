package com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.repository;

import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.entity.FranquiciaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FranquiciaJpaRepository extends JpaRepository<FranquiciaEntity, Long> {

    boolean existsByNombre(String nombre);

    @EntityGraph(attributePaths = {"sucursales", "sucursales.productos"})
    Optional<FranquiciaEntity> findById(Long id);
}
