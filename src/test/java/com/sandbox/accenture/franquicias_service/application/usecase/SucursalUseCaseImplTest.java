package com.sandbox.accenture.franquicias_service.application.usecase;

import com.sandbox.accenture.franquicias_service.domain.exception.DuplicateResourceException;
import com.sandbox.accenture.franquicias_service.domain.exception.ResourceNotFoundException;
import com.sandbox.accenture.franquicias_service.domain.model.Franquicia;
import com.sandbox.accenture.franquicias_service.domain.model.Sucursal;
import com.sandbox.accenture.franquicias_service.domain.port.out.FranquiciaRepositoryPort;
import com.sandbox.accenture.franquicias_service.domain.port.out.SucursalRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SucursalUseCaseImpl — Tests unitarios")
class SucursalUseCaseImplTest {

    @Mock
    private FranquiciaRepositoryPort franquiciaRepositoryPort;

    @Mock
    private SucursalRepositoryPort sucursalRepositoryPort;

    @InjectMocks
    private SucursalUseCaseImpl useCase;

    private Sucursal sucursalGuardada;

    @BeforeEach
    void setUp() {
        sucursalGuardada = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal Norte")
                .franquiciaId(1L)
                .build();
    }

    // ─── agregarSucursal ──────────────────────────────────────────────────────

    @Nested
    @DisplayName("agregarSucursal")
    class AgregarSucursal {

        @Test
        @DisplayName("Agrega sucursal correctamente cuando la franquicia existe y el nombre no está duplicado")
        void deberiaAgregarSucursalExitosamente() {
            // Franquicia con lista vacía → tieneSucursalConNombre() retorna false
            Franquicia franquicia = Franquicia.builder().id(1L).sucursales(new ArrayList<>()).build();
            when(franquiciaRepositoryPort.findById(1L)).thenReturn(Optional.of(franquicia));
            when(sucursalRepositoryPort.save(any())).thenReturn(sucursalGuardada);

            Sucursal resultado = useCase.agregarSucursal(1L, "Sucursal Norte");

            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNombre()).isEqualTo("Sucursal Norte");
            assertThat(resultado.getFranquiciaId()).isEqualTo(1L);
            verify(sucursalRepositoryPort).save(any(Sucursal.class));
            verify(sucursalRepositoryPort, never()).existsByNombreAndFranquiciaId(any(), any());
        }

        @Test
        @DisplayName("Lanza ResourceNotFoundException si la franquicia no existe")
        void deberiaLanzarExcepcionSiFranquiciaNoExiste() {
            when(franquiciaRepositoryPort.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.agregarSucursal(99L, "Sucursal Norte"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");

            verify(sucursalRepositoryPort, never()).save(any());
        }

        @Test
        @DisplayName("Lanza DuplicateResourceException si el nombre ya existe en la franquicia")
        void deberiaLanzarExcepcionSiNombreDuplicadoEnFranquicia() {
            // Franquicia que ya contiene una sucursal con el mismo nombre → tieneSucursalConNombre() retorna true
            Sucursal existente = Sucursal.builder().id(5L).nombre("Sucursal Norte").franquiciaId(1L).build();
            Franquicia franquicia = Franquicia.builder().id(1L).sucursales(new ArrayList<>(List.of(existente))).build();
            when(franquiciaRepositoryPort.findById(1L)).thenReturn(Optional.of(franquicia));

            assertThatThrownBy(() -> useCase.agregarSucursal(1L, "Sucursal Norte"))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("Sucursal Norte");

            verify(sucursalRepositoryPort, never()).save(any());
            verify(sucursalRepositoryPort, never()).existsByNombreAndFranquiciaId(any(), any());
        }
    }

    // ─── actualizarSucursal ───────────────────────────────────────────────────

    @Nested
    @DisplayName("actualizarSucursal")
    class ActualizarSucursal {

        @Test
        @DisplayName("Actualiza el nombre correctamente")
        void deberiaActualizarNombreExitosamente() {
            Sucursal actualizada = Sucursal.builder().id(1L).nombre("Sucursal Sur").franquiciaId(1L).build();

            when(sucursalRepositoryPort.findById(1L)).thenReturn(Optional.of(sucursalGuardada));
            when(sucursalRepositoryPort.existsByNombreAndFranquiciaId("Sucursal Sur", 1L)).thenReturn(false);
            when(sucursalRepositoryPort.save(any())).thenReturn(actualizada);

            Sucursal resultado = useCase.actualizarSucursal(1L, "Sucursal Sur");

            assertThat(resultado.getNombre()).isEqualTo("Sucursal Sur");
        }

        @Test
        @DisplayName("Lanza ResourceNotFoundException si la sucursal no existe")
        void deberiaLanzarExcepcionSiSucursalNoExiste() {
            when(sucursalRepositoryPort.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.actualizarSucursal(99L, "Nuevo"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");
        }

        @Test
        @DisplayName("Lanza DuplicateResourceException si el nuevo nombre ya existe en la franquicia")
        void deberiaLanzarExcepcionSiNuevoNombreDuplicado() {
            when(sucursalRepositoryPort.findById(1L)).thenReturn(Optional.of(sucursalGuardada));
            when(sucursalRepositoryPort.existsByNombreAndFranquiciaId("Sucursal Este", 1L)).thenReturn(true);

            assertThatThrownBy(() -> useCase.actualizarSucursal(1L, "Sucursal Este"))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("Sucursal Este");
        }
    }
}
