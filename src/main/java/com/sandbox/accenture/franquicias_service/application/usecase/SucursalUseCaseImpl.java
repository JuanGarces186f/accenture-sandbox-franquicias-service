package com.sandbox.accenture.franquicias_service.application.usecase;

import com.sandbox.accenture.franquicias_service.domain.exception.DuplicateResourceException;
import com.sandbox.accenture.franquicias_service.domain.exception.ResourceNotFoundException;
import com.sandbox.accenture.franquicias_service.domain.model.Franquicia;
import com.sandbox.accenture.franquicias_service.domain.model.Sucursal;
import com.sandbox.accenture.franquicias_service.domain.port.in.SucursalUseCase;
import com.sandbox.accenture.franquicias_service.domain.port.out.FranquiciaRepositoryPort;
import com.sandbox.accenture.franquicias_service.domain.port.out.SucursalRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SucursalUseCaseImpl implements SucursalUseCase {

    private final FranquiciaRepositoryPort franquiciaRepositoryPort;
    private final SucursalRepositoryPort sucursalRepositoryPort;

    @Autowired
    public SucursalUseCaseImpl(FranquiciaRepositoryPort franquiciaRepositoryPort,
                               SucursalRepositoryPort sucursalRepositoryPort) {
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
        this.sucursalRepositoryPort = sucursalRepositoryPort;
    }

    @Override
    @Transactional
    public Sucursal agregarSucursal(Long franquiciaId, String nombre) {
        log.debug("Agregando sucursal '{}' a franquicia id: {}", nombre, franquiciaId);

        Franquicia franquicia = franquiciaRepositoryPort.findById(franquiciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Franquicia", franquiciaId));

        // Validación de duplicado en el modelo de dominio, sin consulta adicional a la BD
        if (franquicia.tieneSucursalConNombre(nombre)) {
            throw new DuplicateResourceException("Sucursal", nombre);
        }

        Sucursal sucursal = Sucursal.builder().nombre(nombre).franquiciaId(franquiciaId).build();
        Sucursal saved = sucursalRepositoryPort.save(sucursal);
        log.info("Sucursal creada con id: {} en franquicia id: {}", saved.getId(), franquiciaId);
        return saved;
    }

    @Override
    @Transactional
    public Sucursal actualizarSucursal(Long sucursalId, String nuevoNombre) {
        log.debug("Actualizando sucursal id: {}", sucursalId);
        Sucursal sucursal = sucursalRepositoryPort.findById(sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", sucursalId));
        if (nuevoNombre != null) {
            if (sucursalRepositoryPort.existsByNombreAndFranquiciaId(nuevoNombre, sucursal.getFranquiciaId())
                    && !sucursal.getNombre().equals(nuevoNombre)) {
                throw new DuplicateResourceException("Sucursal", nuevoNombre);
            }
            sucursal.setNombre(nuevoNombre);
        }
        Sucursal updated = sucursalRepositoryPort.save(sucursal);
        log.info("Sucursal id: {} actualizada", sucursalId);
        return updated;
    }
}
