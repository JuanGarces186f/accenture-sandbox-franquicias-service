package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.controller;

import com.sandbox.accenture.franquicias_service.domain.model.Producto;
import com.sandbox.accenture.franquicias_service.domain.port.in.ProductoUseCase;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.docs.ProductoControllerDocs;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.ProductoUpdateRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ApiResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ProductoResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.mapper.ProductoWebMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController implements ProductoControllerDocs {

    private final ProductoUseCase productoUseCase;
    private final ProductoWebMapper productoWebMapper;

    @Autowired
    public ProductoController(ProductoUseCase productoUseCase,
                              ProductoWebMapper productoWebMapper) {
        this.productoUseCase = productoUseCase;
        this.productoWebMapper = productoWebMapper;
    }

    @Override
    @DeleteMapping("/{productoId}")
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(@PathVariable Long productoId) {
        productoUseCase.eliminarProducto(productoId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Producto eliminado exitosamente"));
    }

    @Override
    @PatchMapping("/{productoId}")
    public ResponseEntity<ApiResponse<ProductoResponse>> actualizarProducto(
            @PathVariable Long productoId,
            @Valid @RequestBody ProductoUpdateRequest request) {
        Producto producto = productoUseCase.actualizarProducto(productoId, request.getNombre(), request.getStock());
        return ResponseEntity.ok(ApiResponse.ok(productoWebMapper.toResponse(producto), "Producto actualizado exitosamente"));
    }
}
