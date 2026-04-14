package com.sandbox.accenture.franquicias_service.domain.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Franquicia {

    private Long id;
    private String nombre;

    @Builder.Default
    private List<Sucursal> sucursales = new ArrayList<>();

    /**
     * Lógica de negocio pura: valida si ya existe una sucursal con ese nombre
     * dentro de esta franquicia, sin necesidad de consultar la base de datos.
     */
    public boolean tieneSucursalConNombre(String nombre) {
        return sucursales.stream()
                .anyMatch(s -> s.getNombre().equalsIgnoreCase(nombre));
    }
}
