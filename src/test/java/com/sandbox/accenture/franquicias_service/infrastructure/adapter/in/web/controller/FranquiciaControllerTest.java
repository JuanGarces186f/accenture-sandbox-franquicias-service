package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandbox.accenture.franquicias_service.domain.exception.DuplicateResourceException;
import com.sandbox.accenture.franquicias_service.domain.exception.ResourceNotFoundException;
import com.sandbox.accenture.franquicias_service.domain.model.Franquicia;
import com.sandbox.accenture.franquicias_service.domain.model.TopProductoPorSucursal;
import com.sandbox.accenture.franquicias_service.domain.port.in.FranquiciaUseCase;
import com.sandbox.accenture.franquicias_service.domain.port.in.ProductoUseCase;
import com.sandbox.accenture.franquicias_service.domain.port.in.SucursalUseCase;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.FranquiciaRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.FranquiciaUpdateRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.FranquiciaResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.FranquiciaSimpleResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.TopProductoPorSucursalResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.mapper.FranquiciaWebMapper;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.mapper.ProductoWebMapper;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.mapper.SucursalWebMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FranquiciaController.class)
@DisplayName("FranquiciaController — Tests de controlador")
class FranquiciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FranquiciaUseCase franquiciaUseCase;

    @MockitoBean
    private SucursalUseCase sucursalUseCase;

    @MockitoBean
    private ProductoUseCase productoUseCase;

    @MockitoBean
    private FranquiciaWebMapper franquiciaWebMapper;

    @MockitoBean
    private SucursalWebMapper sucursalWebMapper;

    @MockitoBean
    private ProductoWebMapper productoWebMapper;

    // ─── POST /api/v1/franquicias ─────────────────────────────────────────────

    @Nested
    @DisplayName("POST /api/v1/franquicias")
    class CrearFranquicia {

        @Test
        @DisplayName("Retorna 201 cuando la franquicia se crea correctamente")
        void deberiaRetornar201AlCrear() throws Exception {
            FranquiciaRequest request = new FranquiciaRequest();
            request.setNombre("McDonald's");

            Franquicia domain = Franquicia.builder().id(1L).nombre("McDonald's").build();
            FranquiciaResponse response = FranquiciaResponse.builder().id(1L).nombre("McDonald's").build();

            when(franquiciaUseCase.crearFranquicia("McDonald's")).thenReturn(domain);
            when(franquiciaWebMapper.toResponse(domain)).thenReturn(response);

            mockMvc.perform(post("/api/v1/franquicias")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.nombre").value("McDonald's"));
        }

        @Test
        @DisplayName("Retorna 409 cuando el nombre ya existe")
        void deberiaRetornar409SiNombreDuplicado() throws Exception {
            FranquiciaRequest request = new FranquiciaRequest();
            request.setNombre("McDonald's");

            when(franquiciaUseCase.crearFranquicia("McDonald's"))
                    .thenThrow(new DuplicateResourceException("Franquicia", "McDonald's"));

            mockMvc.perform(post("/api/v1/franquicias")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("Retorna 400 cuando el nombre está vacío")
        void deberiaRetornar400SiNombreVacio() throws Exception {
            FranquiciaRequest request = new FranquiciaRequest();
            request.setNombre("");

            mockMvc.perform(post("/api/v1/franquicias")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ─── GET /api/v1/franquicias ──────────────────────────────────────────────

    @Nested
    @DisplayName("GET /api/v1/franquicias")
    class ListarFranquicias {

        @Test
        @DisplayName("Retorna 200 con lista de franquicias")
        void deberiaRetornar200ConListado() throws Exception {
            Franquicia domain = Franquicia.builder().id(1L).nombre("McDonald's").build();
            FranquiciaSimpleResponse simpleResponse = new FranquiciaSimpleResponse();
            simpleResponse.setId(1L);
            simpleResponse.setNombre("McDonald's");

            when(franquiciaUseCase.listarFranquicias()).thenReturn(List.of(domain));
            when(franquiciaWebMapper.toSimpleResponse(domain)).thenReturn(simpleResponse);

            mockMvc.perform(get("/api/v1/franquicias"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data[0].id").value(1))
                    .andExpect(jsonPath("$.data[0].nombre").value("McDonald's"));
        }
    }

    // ─── GET /api/v1/franquicias/{id} ─────────────────────────────────────────

    @Nested
    @DisplayName("GET /api/v1/franquicias/{id}")
    class ObtenerFranquicia {

        @Test
        @DisplayName("Retorna 200 cuando la franquicia existe")
        void deberiaRetornar200SiExiste() throws Exception {
            Franquicia domain = Franquicia.builder().id(1L).nombre("McDonald's").build();
            FranquiciaResponse response = FranquiciaResponse.builder().id(1L).nombre("McDonald's").build();

            when(franquiciaUseCase.obtenerPorId(1L)).thenReturn(domain);
            when(franquiciaWebMapper.toResponse(domain)).thenReturn(response);

            mockMvc.perform(get("/api/v1/franquicias/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(1));
        }

        @Test
        @DisplayName("Retorna 404 cuando la franquicia no existe")
        void deberiaRetornar404SiNoExiste() throws Exception {
            when(franquiciaUseCase.obtenerPorId(99L))
                    .thenThrow(new ResourceNotFoundException("Franquicia", 99L));

            mockMvc.perform(get("/api/v1/franquicias/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false));
        }
    }

    // ─── PATCH /api/v1/franquicias/{id} ──────────────────────────────────────

    @Nested
    @DisplayName("PATCH /api/v1/franquicias/{id}")
    class ActualizarFranquicia {

        @Test
        @DisplayName("Retorna 200 cuando se actualiza correctamente")
        void deberiaRetornar200AlActualizar() throws Exception {
            FranquiciaUpdateRequest request = new FranquiciaUpdateRequest();
            request.setNombre("Burger King");

            Franquicia domain = Franquicia.builder().id(1L).nombre("Burger King").build();
            FranquiciaResponse response = FranquiciaResponse.builder().id(1L).nombre("Burger King").build();

            when(franquiciaUseCase.actualizarFranquicia(eq(1L), eq("Burger King"))).thenReturn(domain);
            when(franquiciaWebMapper.toResponse(domain)).thenReturn(response);

            mockMvc.perform(patch("/api/v1/franquicias/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.nombre").value("Burger King"));
        }

        @Test
        @DisplayName("Retorna 404 cuando la franquicia no existe")
        void deberiaRetornar404SiNoExiste() throws Exception {
            FranquiciaUpdateRequest request = new FranquiciaUpdateRequest();
            request.setNombre("Nuevo");

            when(franquiciaUseCase.actualizarFranquicia(eq(99L), eq("Nuevo")))
                    .thenThrow(new ResourceNotFoundException("Franquicia", 99L));

            mockMvc.perform(patch("/api/v1/franquicias/99")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }
    }

    // ─── GET /api/v1/franquicias/{id}/productos/top-stock ────────────────────

    @Nested
    @DisplayName("GET /api/v1/franquicias/{id}/productos/top-stock")
    class TopProductos {

        @Test
        @DisplayName("Retorna 200 con el top de productos por sucursal")
        void deberiaRetornarTopProductos() throws Exception {
            TopProductoPorSucursal domain =
                    new TopProductoPorSucursal(1L, "Sucursal Norte", 1L, "Nuggets", 200);
            TopProductoPorSucursalResponse response =
                    new TopProductoPorSucursalResponse(1L, "Sucursal Norte", 1L, "Nuggets", 200);

            when(productoUseCase.obtenerTopProductosPorFranquicia(1L)).thenReturn(List.of(domain));
            when(productoWebMapper.toTopResponse(domain)).thenReturn(response);

            mockMvc.perform(get("/api/v1/franquicias/1/productos/top-stock"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].sucursalNombre").value("Sucursal Norte"))
                    .andExpect(jsonPath("$.data[0].productoNombre").value("Nuggets"))
                    .andExpect(jsonPath("$.data[0].stock").value(200));
        }

        @Test
        @DisplayName("Retorna 404 cuando la franquicia no existe")
        void deberiaRetornar404SiFranquiciaNoExiste() throws Exception {
            when(productoUseCase.obtenerTopProductosPorFranquicia(99L))
                    .thenThrow(new ResourceNotFoundException("Franquicia", 99L));

            mockMvc.perform(get("/api/v1/franquicias/99/productos/top-stock"))
                    .andExpect(status().isNotFound());
        }
    }
}
