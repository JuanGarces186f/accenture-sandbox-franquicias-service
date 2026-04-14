package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.docs;

import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.FranquiciaRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.FranquiciaUpdateRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.SucursalRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ApiResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ErrorApiResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.FranquiciaResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.FranquiciaSimpleResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.SucursalResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.TopProductoPorSucursalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Franquicias", description = "Gestión de franquicias, sus sucursales y consulta de top productos")
public interface FranquiciaControllerDocs {

    @Operation(summary = "Crear franquicia", description = "Crea una nueva franquicia con el nombre proporcionado")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Franquicia creada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Ya existe una franquicia con ese nombre",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<ApiResponse<FranquiciaResponse>> crearFranquicia(@Valid @RequestBody FranquiciaRequest request);

    @Operation(summary = "Listar franquicias", description = "Retorna todas las franquicias con solo id y nombre")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    ResponseEntity<ApiResponse<List<FranquiciaSimpleResponse>>> listarFranquicias();

    @Operation(summary = "Obtener franquicia por ID", description = "Obtiene una franquicia completa con todas sus sucursales y productos")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Franquicia obtenida exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Franquicia no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<ApiResponse<FranquiciaResponse>> obtenerFranquicia(
            @Parameter(description = "ID de la franquicia", required = true, example = "1") @PathVariable Long id);

    @Operation(summary = "Actualizar franquicia", description = "Actualización parcial PATCH")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Franquicia actualizada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Franquicia no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Ya existe una franquicia con ese nombre",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<ApiResponse<FranquiciaResponse>> actualizarFranquicia(
            @Parameter(description = "ID de la franquicia", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody FranquiciaUpdateRequest request);

    @Operation(summary = "Agregar sucursal a franquicia", description = "Agrega una nueva sucursal a la franquicia indicada")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Sucursal agregada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Franquicia no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Ya existe una sucursal con ese nombre en la franquicia",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<ApiResponse<SucursalResponse>> agregarSucursal(
            @Parameter(description = "ID de la franquicia", required = true, example = "1") @PathVariable Long franquiciaId,
            @Valid @RequestBody SucursalRequest request);

    @Operation(summary = "Top producto por sucursal", description = "Retorna el producto con mayor stock por cada sucursal de la franquicia")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Consulta exitosa"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Franquicia no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<ApiResponse<List<TopProductoPorSucursalResponse>>> obtenerTopProductosPorFranquicia(
            @Parameter(description = "ID de la franquicia", required = true, example = "1") @PathVariable Long franquiciaId);
}
