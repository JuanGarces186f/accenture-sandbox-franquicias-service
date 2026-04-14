package com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.mapper;

import com.sandbox.accenture.franquicias_service.domain.model.Sucursal;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProductoEntityMapper.class)
public interface SucursalEntityMapper {

    @Mapping(target = "franquiciaId", source = "franquicia.id")
    Sucursal toDomain(SucursalEntity entity);

    @Mapping(target = "franquicia", ignore = true)
    @Mapping(target = "productos", ignore = true)
    SucursalEntity toEntity(Sucursal domain);
}
