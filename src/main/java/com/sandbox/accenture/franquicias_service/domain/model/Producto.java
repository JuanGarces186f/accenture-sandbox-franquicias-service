package com.sandbox.accenture.franquicias_service.domain.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Producto {
    private Long id;
    private String nombre;
    private Integer stock;
    private Long sucursalId;
}
