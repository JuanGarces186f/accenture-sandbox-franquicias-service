package com.sandbox.accenture.franquicias_service.domain.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sucursal {

    private Long id;
    private String nombre;
    private Long franquiciaId;

    @Builder.Default
    private List<Producto> productos = new ArrayList<>();

    /**
     * Lógica de negocio pura: valida si ya existe un producto con ese nombre
     * dentro de esta sucursal, sin necesidad de consultar la base de datos.
     */
    public boolean tieneProductoConNombre(String nombre) {
        return productos.stream()
                .anyMatch(p -> p.getNombre().equalsIgnoreCase(nombre));
    }
}
