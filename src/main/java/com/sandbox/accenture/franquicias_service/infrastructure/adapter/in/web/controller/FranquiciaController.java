package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.controller;

import com.sandbox.accenture.franquicias_service.domain.model.Franquicia;
import com.sandbox.accenture.franquicias_service.domain.model.Sucursal;

import com.sandbox.accenture.franquicias_service.domain.port.in.FranquiciaUseCase;
import com.sandbox.accenture.franquicias_service.domain.port.in.ProductoUseCase;
import com.sandbox.accenture.franquicias_service.domain.port.in.SucursalUseCase;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.docs.FranquiciaControllerDocs;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.FranquiciaRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.FranquiciaUpdateRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.request.SucursalRequest;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ApiResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.FranquiciaResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.FranquiciaSimpleResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.SucursalResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.TopProductoPorSucursalResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.mapper.FranquiciaWebMapper;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.mapper.ProductoWebMapper;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.mapper.SucursalWebMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/franquicias")
public class FranquiciaController implements FranquiciaControllerDocs {

    private final FranquiciaUseCase franquiciaUseCase;
    private final SucursalUseCase sucursalUseCase;
    private final ProductoUseCase productoUseCase;
    private final FranquiciaWebMapper franquiciaWebMapper;
    private final SucursalWebMapper sucursalWebMapper;
    private final ProductoWebMapper productoWebMapper;

    @Autowired
    public FranquiciaController(FranquiciaUseCase franquiciaUseCase,
                                SucursalUseCase sucursalUseCase,
                                ProductoUseCase productoUseCase,
                                FranquiciaWebMapper franquiciaWebMapper,
                                SucursalWebMapper sucursalWebMapper,
                                ProductoWebMapper productoWebMapper) {
        this.franquiciaUseCase = franquiciaUseCase;
        this.sucursalUseCase = sucursalUseCase;
        this.productoUseCase = productoUseCase;
        this.franquiciaWebMapper = franquiciaWebMapper;
        this.sucursalWebMapper = sucursalWebMapper;
        this.productoWebMapper = productoWebMapper;
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<FranquiciaResponse>> crearFranquicia(
            @Valid @RequestBody FranquiciaRequest request) {
        Franquicia franquicia = franquiciaUseCase.crearFranquicia(request.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(franquiciaWebMapper.toResponse(franquicia), "Franquicia creada exitosamente"));
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<FranquiciaSimpleResponse>>> listarFranquicias() {
        List<FranquiciaSimpleResponse> list = franquiciaUseCase.listarFranquicias().stream()
                .map(franquiciaWebMapper::toSimpleResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(list, "Franquicias obtenidas exitosamente"));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FranquiciaResponse>> obtenerFranquicia(@PathVariable Long id) {
        Franquicia franquicia = franquiciaUseCase.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.ok(franquiciaWebMapper.toResponse(franquicia), "Franquicia obtenida exitosamente"));
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<FranquiciaResponse>> actualizarFranquicia(
            @PathVariable Long id,
            @Valid @RequestBody FranquiciaUpdateRequest request) {
        Franquicia franquicia = franquiciaUseCase.actualizarFranquicia(id, request.getNombre());
        return ResponseEntity.ok(ApiResponse.ok(franquiciaWebMapper.toResponse(franquicia), "Franquicia actualizada exitosamente"));
    }

    @Override
    @PostMapping("/{franquiciaId}/sucursales")
    public ResponseEntity<ApiResponse<SucursalResponse>> agregarSucursal(
            @PathVariable Long franquiciaId,
            @Valid @RequestBody SucursalRequest request) {
        Sucursal sucursal = sucursalUseCase.agregarSucursal(franquiciaId, request.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(sucursalWebMapper.toResponse(sucursal), "Sucursal agregada exitosamente"));
    }

    @Override
    @GetMapping("/{franquiciaId}/productos/top-stock")
    public ResponseEntity<ApiResponse<List<TopProductoPorSucursalResponse>>> obtenerTopProductosPorFranquicia(
            @PathVariable Long franquiciaId) {
        List<TopProductoPorSucursalResponse> result = productoUseCase
                .obtenerTopProductosPorFranquicia(franquiciaId).stream()
                .map(productoWebMapper::toTopResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(result, "Top productos por sucursal obtenidos exitosamente"));
    }
}
