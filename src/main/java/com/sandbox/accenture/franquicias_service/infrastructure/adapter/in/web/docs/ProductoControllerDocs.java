package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.docs;

import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.ProductoUpdateRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ApiResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ErrorApiResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ProductoResponse;
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

@Tag(name = "Productos", description = "Gestión individual de productos: eliminación y actualización")
public interface ProductoControllerDocs {

    @Operation(summary = "Eliminar producto", description = "Elimina un producto por su ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<ApiResponse<Void>> eliminarProducto(
            @Parameter(description = "ID del producto", required = true, example = "1") @PathVariable Long productoId);

    @Operation(summary = "Actualizar producto", description = "Actualización parcial PATCH: envía solo los campos a modificar, los null se ignoran")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Ya existe un producto con ese nombre en la sucursal",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<ApiResponse<ProductoResponse>> actualizarProducto(
            @Parameter(description = "ID del producto", required = true, example = "1") @PathVariable Long productoId,
            @Valid @RequestBody ProductoUpdateRequest request);
}
