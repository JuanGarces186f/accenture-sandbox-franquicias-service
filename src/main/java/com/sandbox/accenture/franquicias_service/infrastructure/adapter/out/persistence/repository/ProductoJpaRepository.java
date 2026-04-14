package com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.repository;

import com.sandbox.accenture.franquicias_service.domain.model.TopProductoPorSucursal;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoJpaRepository extends JpaRepository<ProductoEntity, Long> {

    boolean existsByNombreAndSucursalId(String nombre, Long sucursalId);

    @EntityGraph(attributePaths = {"sucursal"})
    Optional<ProductoEntity> findById(Long id);

    @Query("""
            SELECT new com.sandbox.accenture.franquicias_service.domain.model.TopProductoPorSucursal(
                s.id, s.nombre, p.id, p.nombre, p.stock
            )
            FROM ProductoEntity p
            JOIN p.sucursal s
            WHERE s.franquicia.id = :franquiciaId
              AND p.stock = (
                  SELECT MAX(p2.stock) FROM ProductoEntity p2 WHERE p2.sucursal.id = s.id
              )
            ORDER BY s.id
            """)
    List<TopProductoPorSucursal> findTopStockProductosByFranquicia(@Param("franquiciaId") Long franquiciaId);
}
