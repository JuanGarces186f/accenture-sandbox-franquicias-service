package com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.mapper;

import com.sandbox.accenture.franquicias_service.domain.model.Producto;
import com.sandbox.accenture.franquicias_service.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductoEntityMapper {

    @Mapping(target = "sucursalId", source = "sucursal.id")
    Producto toDomain(ProductoEntity entity);

    @Mapping(target = "sucursal", ignore = true)
    ProductoEntity toEntity(Producto domain);
}
