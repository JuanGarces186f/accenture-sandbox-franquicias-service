package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.controller;

import com.sandbox.accenture.franquicias_service.domain.model.Producto;
import com.sandbox.accenture.franquicias_service.domain.model.Sucursal;
import com.sandbox.accenture.franquicias_service.domain.port.in.ProductoUseCase;
import com.sandbox.accenture.franquicias_service.domain.port.in.SucursalUseCase;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.docs.SucursalControllerDocs;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.ProductoRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.SucursalUpdateRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ApiResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ProductoResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.SucursalResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.mapper.ProductoWebMapper;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.mapper.SucursalWebMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sucursales")
public class SucursalController implements SucursalControllerDocs {

    private final SucursalUseCase sucursalUseCase;
    private final ProductoUseCase productoUseCase;
    private final SucursalWebMapper sucursalWebMapper;
    private final ProductoWebMapper productoWebMapper;

    @Autowired
    public SucursalController(SucursalUseCase sucursalUseCase,
                              ProductoUseCase productoUseCase,
                              SucursalWebMapper sucursalWebMapper,
                              ProductoWebMapper productoWebMapper) {
        this.sucursalUseCase = sucursalUseCase;
        this.productoUseCase = productoUseCase;
        this.sucursalWebMapper = sucursalWebMapper;
        this.productoWebMapper = productoWebMapper;
    }

    @Override
    @PatchMapping("/{sucursalId}")
    public ResponseEntity<ApiResponse<SucursalResponse>> actualizarSucursal(
            @PathVariable Long sucursalId,
            @Valid @RequestBody SucursalUpdateRequest request) {
        Sucursal sucursal = sucursalUseCase.actualizarSucursal(sucursalId, request.getNombre());
        return ResponseEntity.ok(ApiResponse.ok(sucursalWebMapper.toResponse(sucursal), "Sucursal actualizada exitosamente"));
    }

    @Override
    @PostMapping("/{sucursalId}/productos")
    public ResponseEntity<ApiResponse<ProductoResponse>> agregarProducto(
            @PathVariable Long sucursalId,
            @Valid @RequestBody ProductoRequest request) {
        Producto producto = productoUseCase.agregarProducto(sucursalId, request.getNombre(), request.getStock());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(productoWebMapper.toResponse(producto), "Producto agregado exitosamente"));
    }
}
