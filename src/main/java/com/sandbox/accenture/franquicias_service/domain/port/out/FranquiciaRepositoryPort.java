package com.sandbox.accenture.franquicias_service.domain.port.out;

import com.sandbox.accenture.franquicias_service.domain.model.Franquicia;

import java.util.List;
import java.util.Optional;

public interface FranquiciaRepositoryPort {

    boolean existsByNombre(String nombre);

    Optional<Franquicia> findById(Long id);

    List<Franquicia> findAll();

    Franquicia save(Franquicia franquicia);

    boolean existsById(Long id);
}
