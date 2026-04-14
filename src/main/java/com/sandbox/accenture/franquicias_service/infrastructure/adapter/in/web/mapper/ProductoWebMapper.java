package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.mapper;

import com.sandbox.accenture.franquicias_service.domain.model.Producto;
import com.sandbox.accenture.franquicias_service.domain.model.TopProductoPorSucursal;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.ProductoResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.TopProductoPorSucursalResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoWebMapper {

    ProductoResponse toResponse(Producto domain);

    TopProductoPorSucursalResponse toTopResponse(TopProductoPorSucursal domain);
}
