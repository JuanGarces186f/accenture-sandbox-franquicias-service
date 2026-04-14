package com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.repository;

import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SucursalJpaRepository extends JpaRepository<SucursalEntity, Long> {

    boolean existsByNombreAndFranquiciaId(String nombre, Long franquiciaId);

    @EntityGraph(attributePaths = {"franquicia"})
    Optional<SucursalEntity> findById(Long id);
}
