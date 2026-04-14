package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.mapper;

import com.sandbox.accenture.franquicias_service.domain.model.Sucursal;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.SucursalResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ProductoWebMapper.class)
public interface SucursalWebMapper {

    SucursalResponse toResponse(Sucursal domain);
}
