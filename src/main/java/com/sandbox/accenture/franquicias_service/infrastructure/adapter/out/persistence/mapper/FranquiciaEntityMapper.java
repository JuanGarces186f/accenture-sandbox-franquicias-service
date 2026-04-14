package com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.mapper;

import com.sandbox.accenture.franquicias_service.domain.model.Franquicia;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.entity.FranquiciaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = SucursalEntityMapper.class)
public interface FranquiciaEntityMapper {

    Franquicia toDomain(FranquiciaEntity entity);

    @Mapping(target = "sucursales", ignore = true)
    FranquiciaEntity toEntity(Franquicia domain);
}
