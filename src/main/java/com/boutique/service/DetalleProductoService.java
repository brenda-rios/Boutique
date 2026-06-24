package com.boutique.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutique.dto.DetalleProductoDTO;
import com.boutique.model.DetalleProducto;
import com.boutique.repo.DetalleProductoRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DetalleProductoService {
    @Autowired
    DetalleProductoRepo detalleProductoRepo;
    @Autowired
    ModelMapper mapper;

    public List<DetalleProductoDTO> listar() {
        return detalleProductoRepo.findAll().stream()
                .map(detalle -> mapper.map(detalle, DetalleProductoDTO.class))
                .toList();
    }

    public void guardar(DetalleProductoDTO detalleProducto) {
        detalleProductoRepo.save(mapper.map(detalleProducto, DetalleProducto.class));
    }
    
    public void actualizar(DetalleProductoDTO detalle) {
		Optional<DetalleProducto> OptDetalle = detalleProductoRepo.findByUuid(detalle.getUuid());
		if (OptDetalle.isPresent()) {
			mapper.map(detalle, OptDetalle.get());
			detalleProductoRepo.save(OptDetalle.get());
		} else {
			throw new EntityNotFoundException("Detalle no encontrado con UUID: " + detalle.getUuid());
		}	
	}
	
	public void borrar(UUID uuid) {
		Optional<DetalleProducto> OptDetalle = detalleProductoRepo.findByUuid(uuid);
		if (OptDetalle.isPresent()) {
			detalleProductoRepo.delete(OptDetalle.get());
		} else {
			throw new EntityNotFoundException("Detalle no encontrado con UUID: " + uuid);
		}
	}
	
	public DetalleProductoDTO obtenerDetalleProductoUUID(UUID uuid) {
		Optional<DetalleProducto> OptDetalle = detalleProductoRepo.findByUuid(uuid);
		if (OptDetalle.isPresent()) {
			return mapper.map(OptDetalle.get(), DetalleProductoDTO.class);
		} else {
			throw new EntityNotFoundException("Detalle no encontrado con UUID: " + uuid);
		}
	}
}
