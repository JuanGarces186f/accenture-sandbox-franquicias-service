package com.sandbox.accenture.franquicias_service.application.usecase;

import com.sandbox.accenture.franquicias_service.domain.exception.DuplicateResourceException;
import com.sandbox.accenture.franquicias_service.domain.exception.ResourceNotFoundException;
import com.sandbox.accenture.franquicias_service.domain.model.Producto;
import com.sandbox.accenture.franquicias_service.domain.model.Sucursal;
import com.sandbox.accenture.franquicias_service.domain.model.TopProductoPorSucursal;

import java.util.ArrayList;
import com.sandbox.accenture.franquicias_service.domain.port.out.FranquiciaRepositoryPort;
import com.sandbox.accenture.franquicias_service.domain.port.out.ProductoRepositoryPort;
import com.sandbox.accenture.franquicias_service.domain.port.out.SucursalRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductoUseCaseImpl — Tests unitarios")
class ProductoUseCaseImplTest {

    @Mock
    private SucursalRepositoryPort sucursalRepositoryPort;

    @Mock
    private ProductoRepositoryPort productoRepositoryPort;

    @Mock
    private FranquiciaRepositoryPort franquiciaRepositoryPort;

    @InjectMocks
    private ProductoUseCaseImpl useCase;

    private Producto productoGuardado;

    @BeforeEach
    void setUp() {
        productoGuardado = Producto.builder()
                .id(1L)
                .nombre("Hamburguesa Doble")
                .stock(100)
                .sucursalId(1L)
                .build();
    }

    // ─── agregarProducto ──────────────────────────────────────────────────────

    @Nested
    @DisplayName("agregarProducto")
    class AgregarProducto {

        @Test
        @DisplayName("Agrega producto correctamente cuando la sucursal existe y el nombre no está duplicado")
        void deberiaAgregarProductoExitosamente() {
            // Sucursal con lista vacía → tieneProductoConNombre() retorna false
            Sucursal sucursal = Sucursal.builder().id(1L).productos(new ArrayList<>()).build();
            when(sucursalRepositoryPort.findById(1L)).thenReturn(Optional.of(sucursal));
            when(productoRepositoryPort.save(any())).thenReturn(productoGuardado);

            Producto resultado = useCase.agregarProducto(1L, "Hamburguesa Doble", 100);

            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNombre()).isEqualTo("Hamburguesa Doble");
            assertThat(resultado.getStock()).isEqualTo(100);
            verify(productoRepositoryPort).save(any(Producto.class));
            verify(productoRepositoryPort, never()).existsByNombreAndSucursalId(any(), any());
        }

        @Test
        @DisplayName("Lanza ResourceNotFoundException si la sucursal no existe")
        void deberiaLanzarExcepcionSiSucursalNoExiste() {
            when(sucursalRepositoryPort.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.agregarProducto(99L, "Hamburguesa", 10))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");

            verify(productoRepositoryPort, never()).save(any());
        }

        @Test
        @DisplayName("Lanza DuplicateResourceException si el nombre ya existe en la sucursal")
        void deberiaLanzarExcepcionSiNombreDuplicadoEnSucursal() {
            // Sucursal que ya contiene un producto con el mismo nombre → tieneProductoConNombre() retorna true
            Producto existente = Producto.builder().id(10L).nombre("Hamburguesa Doble").stock(50).sucursalId(1L).build();
            Sucursal sucursal = Sucursal.builder().id(1L).productos(new ArrayList<>(List.of(existente))).build();
            when(sucursalRepositoryPort.findById(1L)).thenReturn(Optional.of(sucursal));

            assertThatThrownBy(() -> useCase.agregarProducto(1L, "Hamburguesa Doble", 100))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("Hamburguesa Doble");

            verify(productoRepositoryPort, never()).save(any());
            verify(productoRepositoryPort, never()).existsByNombreAndSucursalId(any(), any());
        }
    }

    // ─── eliminarProducto ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("eliminarProducto")
    class EliminarProducto {

        @Test
        @DisplayName("Elimina el producto correctamente")
        void deberiaEliminarProductoExitosamente() {
            when(productoRepositoryPort.findById(1L)).thenReturn(Optional.of(productoGuardado));

            useCase.eliminarProducto(1L);

            verify(productoRepositoryPort).delete(productoGuardado);
        }

        @Test
        @DisplayName("Lanza ResourceNotFoundException si el producto no existe")
        void deberiaLanzarExcepcionSiProductoNoExiste() {
            when(productoRepositoryPort.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.eliminarProducto(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");

            verify(productoRepositoryPort, never()).delete(any());
        }
    }

    // ─── actualizarProducto ───────────────────────────────────────────────────

    @Nested
    @DisplayName("actualizarProducto")
    class ActualizarProducto {

        @Test
        @DisplayName("Actualiza nombre y stock correctamente")
        void deberiaActualizarNombreYStockExitosamente() {
            Producto actualizado = Producto.builder()
                    .id(1L).nombre("Hamburguesa Triple").stock(200).sucursalId(1L).build();

            when(productoRepositoryPort.findById(1L)).thenReturn(Optional.of(productoGuardado));
            when(productoRepositoryPort.existsByNombreAndSucursalId("Hamburguesa Triple", 1L)).thenReturn(false);
            when(productoRepositoryPort.save(any())).thenReturn(actualizado);

            Producto resultado = useCase.actualizarProducto(1L, "Hamburguesa Triple", 200);

            assertThat(resultado.getNombre()).isEqualTo("Hamburguesa Triple");
            assertThat(resultado.getStock()).isEqualTo(200);
        }

        @Test
        @DisplayName("Actualiza solo el stock cuando nombre es null")
        void deberiaActualizarSoloStockSiNombreEsNull() {
            Producto actualizado = Producto.builder()
                    .id(1L).nombre("Hamburguesa Doble").stock(300).sucursalId(1L).build();

            when(productoRepositoryPort.findById(1L)).thenReturn(Optional.of(productoGuardado));
            when(productoRepositoryPort.save(any())).thenReturn(actualizado);

            Producto resultado = useCase.actualizarProducto(1L, null, 300);

            assertThat(resultado.getStock()).isEqualTo(300);
            assertThat(resultado.getNombre()).isEqualTo("Hamburguesa Doble");
        }

        @Test
        @DisplayName("Lanza ResourceNotFoundException si el producto no existe")
        void deberiaLanzarExcepcionSiProductoNoExiste() {
            when(productoRepositoryPort.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.actualizarProducto(99L, "Nuevo", 10))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");
        }

        @Test
        @DisplayName("Lanza DuplicateResourceException si el nuevo nombre ya existe en la sucursal")
        void deberiaLanzarExcepcionSiNuevoNombreDuplicado() {
            when(productoRepositoryPort.findById(1L)).thenReturn(Optional.of(productoGuardado));
            when(productoRepositoryPort.existsByNombreAndSucursalId("Nuggets", 1L)).thenReturn(true);

            assertThatThrownBy(() -> useCase.actualizarProducto(1L, "Nuggets", null))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("Nuggets");
        }
    }

    // ─── obtenerTopProductosPorFranquicia ─────────────────────────────────────

    @Nested
    @DisplayName("obtenerTopProductosPorFranquicia")
    class ObtenerTopProductos {

        @Test
        @DisplayName("Retorna top productos por sucursal correctamente")
        void deberiaRetornarTopProductos() {
            TopProductoPorSucursal top = new TopProductoPorSucursal(1L, "Sucursal Norte", 1L, "Hamburguesa Doble", 100);

            when(franquiciaRepositoryPort.existsById(1L)).thenReturn(true);
            when(productoRepositoryPort.findTopStockProductosByFranquicia(1L)).thenReturn(List.of(top));

            List<TopProductoPorSucursal> resultado = useCase.obtenerTopProductosPorFranquicia(1L);

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).productoNombre()).isEqualTo("Hamburguesa Doble");
            assertThat(resultado.get(0).stock()).isEqualTo(100);
        }

        @Test
        @DisplayName("Lanza ResourceNotFoundException si la franquicia no existe")
        void deberiaLanzarExcepcionSiFranquiciaNoExiste() {
            when(franquiciaRepositoryPort.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> useCase.obtenerTopProductosPorFranquicia(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }
}
