package com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.mapper;

import com.sandbox.accenture.franquicias_service.domain.model.Franquicia;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.FranquiciaResponse;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.in.web.dto.response.FranquiciaSimpleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SucursalWebMapper.class)
public interface FranquiciaWebMapper {

    FranquiciaResponse toResponse(Franquicia domain);

    FranquiciaSimpleResponse toSimpleResponse(Franquicia domain);
}
