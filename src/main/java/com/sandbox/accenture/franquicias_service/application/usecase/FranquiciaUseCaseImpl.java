package com.sandbox.accenture.franquicias_service.application.usecase;

import com.sandbox.accenture.franquicias_service.domain.exception.DuplicateResourceException;
import com.sandbox.accenture.franquicias_service.domain.exception.ResourceNotFoundException;
import com.sandbox.accenture.franquicias_service.domain.model.Franquicia;
import com.sandbox.accenture.franquicias_service.domain.port.in.FranquiciaUseCase;
import com.sandbox.accenture.franquicias_service.domain.port.out.FranquiciaRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class FranquiciaUseCaseImpl implements FranquiciaUseCase {

    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    @Autowired
    public FranquiciaUseCaseImpl(FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    @Override
    @Transactional
    public Franquicia crearFranquicia(String nombre) {
        log.debug("Creando franquicia con nombre: {}", nombre);
        if (franquiciaRepositoryPort.existsByNombre(nombre)) {
            throw new DuplicateResourceException("Franquicia", nombre);
        }
        Franquicia franquicia = Franquicia.builder().nombre(nombre).build();
        Franquicia saved = franquiciaRepositoryPort.save(franquicia);
        log.info("Franquicia creada con id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Franquicia actualizarFranquicia(Long id, String nuevoNombre) {
        log.debug("Actualizando franquicia id: {}", id);
        Franquicia franquicia = franquiciaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Franquicia", id));
        if (nuevoNombre != null) {
            if (franquiciaRepositoryPort.existsByNombre(nuevoNombre)
                    && !franquicia.getNombre().equals(nuevoNombre)) {
                throw new DuplicateResourceException("Franquicia", nuevoNombre);
            }
            franquicia.setNombre(nuevoNombre);
        }
        Franquicia updated = franquiciaRepositoryPort.save(franquicia);
        log.info("Franquicia id: {} actualizada", id);
        return updated;
    }

    @Override
    @Transactional(readOnly = true)
    public Franquicia obtenerPorId(Long id) {
        return franquiciaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Franquicia", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Franquicia> listarFranquicias() {
        return franquiciaRepositoryPort.findAll();
    }
}
