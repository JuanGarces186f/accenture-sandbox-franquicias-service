package com.sandbox.accenture.franquicias_service.application.usecase;

import com.sandbox.accenture.franquicias_service.domain.exception.DuplicateResourceException;
import com.sandbox.accenture.franquicias_service.domain.exception.ResourceNotFoundException;
import com.sandbox.accenture.franquicias_service.domain.model.Franquicia;
import com.sandbox.accenture.franquicias_service.domain.port.out.FranquiciaRepositoryPort;
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
@DisplayName("FranquiciaUseCaseImpl — Tests unitarios")
class FranquiciaUseCaseImplTest {

    @Mock
    private FranquiciaRepositoryPort franquiciaRepositoryPort;

    @InjectMocks
    private FranquiciaUseCaseImpl useCase;

    // ─── Datos de prueba reutilizables ────────────────────────────────────────

    private Franquicia franquiciaGuardada;

    @BeforeEach
    void setUp() {
        franquiciaGuardada = Franquicia.builder()
                .id(1L)
                .nombre("McDonald's")
                .build();
    }

    // ─── crearFranquicia ──────────────────────────────────────────────────────

    @Nested
    @DisplayName("crearFranquicia")
    class CrearFranquicia {

        @Test
        @DisplayName("Crea franquicia correctamente cuando el nombre no existe")
        void deberiaCrearFranquiciaExitosamente() {
            when(franquiciaRepositoryPort.existsByNombre("McDonald's")).thenReturn(false);
            when(franquiciaRepositoryPort.save(any())).thenReturn(franquiciaGuardada);

            Franquicia resultado = useCase.crearFranquicia("McDonald's");

            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNombre()).isEqualTo("McDonald's");
            verify(franquiciaRepositoryPort).save(any(Franquicia.class));
        }

        @Test
        @DisplayName("Lanza DuplicateResourceException cuando el nombre ya existe")
        void deberiaLanzarExcepcionSiNombreYaExiste() {
            when(franquiciaRepositoryPort.existsByNombre("McDonald's")).thenReturn(true);

            assertThatThrownBy(() -> useCase.crearFranquicia("McDonald's"))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("McDonald's");

            verify(franquiciaRepositoryPort, never()).save(any());
        }
    }

    // ─── actualizarFranquicia ─────────────────────────────────────────────────

    @Nested
    @DisplayName("actualizarFranquicia")
    class ActualizarFranquicia {

        @Test
        @DisplayName("Actualiza el nombre correctamente")
        void deberiaActualizarNombreExitosamente() {
            Franquicia actualizada = Franquicia.builder().id(1L).nombre("Burger King").build();

            when(franquiciaRepositoryPort.findById(1L)).thenReturn(Optional.of(franquiciaGuardada));
            when(franquiciaRepositoryPort.existsByNombre("Burger King")).thenReturn(false);
            when(franquiciaRepositoryPort.save(any())).thenReturn(actualizada);

            Franquicia resultado = useCase.actualizarFranquicia(1L, "Burger King");

            assertThat(resultado.getNombre()).isEqualTo("Burger King");
            verify(franquiciaRepositoryPort).save(any());
        }

        @Test
        @DisplayName("Lanza ResourceNotFoundException si la franquicia no existe")
        void deberiaLanzarExcepcionSiFranquiciaNoExiste() {
            when(franquiciaRepositoryPort.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.actualizarFranquicia(99L, "Nuevo"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");
        }

        @Test
        @DisplayName("Lanza DuplicateResourceException si el nuevo nombre ya pertenece a otra franquicia")
        void deberiaLanzarExcepcionSiNuevoNombreDuplicado() {
            when(franquiciaRepositoryPort.findById(1L)).thenReturn(Optional.of(franquiciaGuardada));
            when(franquiciaRepositoryPort.existsByNombre("KFC")).thenReturn(true);

            assertThatThrownBy(() -> useCase.actualizarFranquicia(1L, "KFC"))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("KFC");
        }
    }

    // ─── obtenerPorId ─────────────────────────────────────────────────────────

    @Nested
    @DisplayName("obtenerPorId")
    class ObtenerPorId {

        @Test
        @DisplayName("Retorna franquicia cuando existe")
        void deberiaRetornarFranquiciaSiExiste() {
            when(franquiciaRepositoryPort.findById(1L)).thenReturn(Optional.of(franquiciaGuardada));

            Franquicia resultado = useCase.obtenerPorId(1L);

            assertThat(resultado.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Lanza ResourceNotFoundException si no existe")
        void deberiaLanzarExcepcionSiNoExiste() {
            when(franquiciaRepositoryPort.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.obtenerPorId(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    // ─── listarFranquicias ────────────────────────────────────────────────────

    @Nested
    @DisplayName("listarFranquicias")
    class ListarFranquicias {

        @Test
        @DisplayName("Retorna lista de franquicias")
        void deberiaRetornarListaDeFranquicias() {
            when(franquiciaRepositoryPort.findAll()).thenReturn(List.of(franquiciaGuardada));

            List<Franquicia> resultado = useCase.listarFranquicias();

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNombre()).isEqualTo("McDonald's");
        }

        @Test
        @DisplayName("Retorna lista vacía cuando no hay franquicias")
        void deberiaRetornarListaVaciaSiNoHayFranquicias() {
            when(franquiciaRepositoryPort.findAll()).thenReturn(List.of());

            List<Franquicia> resultado = useCase.listarFranquicias();

            assertThat(resultado).isEmpty();
        }
    }
}
