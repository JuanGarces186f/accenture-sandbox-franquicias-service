package com.sandbox.accenture.franquicias_service.domain.port.in;

import com.sandbox.accenture.franquicias_service.domain.model.Franquicia;

import java.util.List;

public interface FranquiciaUseCase {

    Franquicia crearFranquicia(String nombre);

    Franquicia actualizarFranquicia(Long id, String nuevoNombre);

    Franquicia obtenerPorId(Long id);

    List<Franquicia> listarFranquicias();
}
