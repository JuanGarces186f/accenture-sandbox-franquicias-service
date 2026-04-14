package com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.repository;

import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.entity.FranquiciaEntity;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.sandbox.accenture.franquicias_service.domain.model.TopProductoPorSucursal;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("ProductoJpaRepository — Tests de integración JPA")
class ProductoJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductoJpaRepository productoJpaRepository;

    private FranquiciaEntity franquicia;
    private SucursalEntity sucursal;

    @BeforeEach
    void setUp() {
        franquicia = entityManager.persistAndFlush(
                FranquiciaEntity.builder().nombre("McDonald's").build()
        );
        sucursal = entityManager.persistAndFlush(
                SucursalEntity.builder().nombre("Sucursal Norte").franquicia(franquicia).build()
        );
    }

    @Nested
    @DisplayName("existsByNombreAndSucursalId")
    class ExistsByNombre {

        @Test
        @DisplayName("Retorna true cuando el producto existe en la sucursal")
        void deberiaRetornarTrueSiExiste() {
            entityManager.persistAndFlush(
                    ProductoEntity.builder().nombre("Hamburguesa Doble").stock(100).sucursal(sucursal).build()
            );

            boolean existe = productoJpaRepository.existsByNombreAndSucursalId("Hamburguesa Doble", sucursal.getId());

            assertThat(existe).isTrue();
        }

        @Test
        @DisplayName("Retorna false cuando el producto no existe en la sucursal")
        void deberiaRetornarFalseSiNoExiste() {
            boolean existe = productoJpaRepository.existsByNombreAndSucursalId("Inexistente", sucursal.getId());

            assertThat(existe).isFalse();
        }
    }

    @Nested
    @DisplayName("findTopStockProductosByFranquicia")
    class TopStockProductos {

        @Test
        @DisplayName("Retorna el producto con mayor stock por sucursal")
        void deberiaRetornarTopProductoPorSucursal() {
            entityManager.persistAndFlush(
                    ProductoEntity.builder().nombre("Hamburguesa Doble").stock(50).sucursal(sucursal).build()
            );
            entityManager.persistAndFlush(
                    ProductoEntity.builder().nombre("Nuggets").stock(200).sucursal(sucursal).build()
            );
            entityManager.persistAndFlush(
                    ProductoEntity.builder().nombre("Papas").stock(80).sucursal(sucursal).build()
            );

            List<TopProductoPorSucursal> resultado = productoJpaRepository.findTopStockProductosByFranquicia(franquicia.getId());

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).productoNombre()).isEqualTo("Nuggets");
            assertThat(resultado.get(0).stock()).isEqualTo(200);
        }

        @Test
        @DisplayName("Retorna el top por cada sucursal cuando hay varias")
        void deberiaRetornarTopPorCadaSucursal() {
            SucursalEntity sucursal2 = entityManager.persistAndFlush(
                    SucursalEntity.builder().nombre("Sucursal Sur").franquicia(franquicia).build()
            );
            entityManager.persistAndFlush(
                    ProductoEntity.builder().nombre("Nuggets").stock(200).sucursal(sucursal).build()
            );
            entityManager.persistAndFlush(
                    ProductoEntity.builder().nombre("Pizza").stock(150).sucursal(sucursal2).build()
            );

            List<TopProductoPorSucursal> resultado = productoJpaRepository.findTopStockProductosByFranquicia(franquicia.getId());

            assertThat(resultado).hasSize(2);
        }

        @Test
        @DisplayName("Retorna lista vacía si la franquicia no tiene productos")
        void deberiaRetornarListaVaciaSinProductos() {
            List<TopProductoPorSucursal> resultado = productoJpaRepository.findTopStockProductosByFranquicia(franquicia.getId());

            assertThat(resultado).isEmpty();
        }
    }
}
