package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.docs;

import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.ProductoRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.SucursalUpdateRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ApiResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ErrorApiResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ProductoResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.SucursalResponse;
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

@Tag(name = "Sucursales", description = "Gestión de sucursales y sus productos")
public interface SucursalControllerDocs {

    @Operation(summary = "Actualizar sucursal", description = "Actualización parcial PATCH")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sucursal actualizada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Ya existe una sucursal con ese nombre en la franquicia",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<ApiResponse<SucursalResponse>> actualizarSucursal(
            @Parameter(description = "ID de la sucursal", required = true, example = "1") @PathVariable Long sucursalId,
            @Valid @RequestBody SucursalUpdateRequest request);

    @Operation(summary = "Agregar producto a sucursal", description = "Agrega un nuevo producto a la sucursal indicada")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Producto agregado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Ya existe un producto con ese nombre en la sucursal",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<ApiResponse<ProductoResponse>> agregarProducto(
            @Parameter(description = "ID de la sucursal", required = true, example = "1") @PathVariable Long sucursalId,
            @Valid @RequestBody ProductoRequest request);
}
