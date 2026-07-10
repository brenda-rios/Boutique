package com.boutique.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutique.dto.TallaDTO;
import com.boutique.model.Talla;
import com.boutique.repo.TallaRepo;
import com.boutique.repo.DetalleProductoRepo;
import com.boutique.exception.ReferencedEntityException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TallaService {
	@Autowired
    TallaRepo tallaRepo;
    @Autowired
    DetalleProductoRepo detalleProductoRepo;
    @Autowired
    ModelMapper mapper;

    public List<TallaDTO> listar() {
        return tallaRepo.findAll().stream()
                .map(talla -> mapper.map(talla, TallaDTO.class))
                .toList();
    }

    public void guardar(TallaDTO talla) {
        tallaRepo.save(mapper.map(talla, Talla.class));
    }
    public void actualizar(TallaDTO talla) {
        Optional<Talla> OptTalla = tallaRepo.findByUuid(talla.getUuid());
        if (OptTalla.isPresent()) {
            Talla entidadExistente = OptTalla.get();
            
            // Mapeo manual: solo actualizamos lo que el usuario puede cambiar
            entidadExistente.setNombre(talla.getNombre());
            
            // No tocamos el ID ni el UUID, ya que esos son inmutables para el registro
            tallaRepo.save(entidadExistente);
        } else {
            throw new EntityNotFoundException("Talla no encontrada con el UUID: " + talla.getUuid());
        }
    }
	
	public void borrar(UUID uuid) {
		Optional<Talla> OptTalla = tallaRepo.findByUuid(uuid);
		if (OptTalla.isPresent()) {
			Talla talla = OptTalla.get();
			// Validar que no hay detalles de producto asociados
			long detallesCount = detalleProductoRepo.countByTalla(talla);
			if (detallesCount > 0) {
				throw new ReferencedEntityException(
					"No se puede eliminar esta talla porque está siendo utilizada en " + detallesCount + " detalle(s) de producto."
				);
			}
			tallaRepo.delete(talla);
		} else {
			throw new EntityNotFoundException("Talla no encontrada con el UUID: " + uuid);
		}
	}
	
	public TallaDTO obtenerTallaUUID(UUID uuid) {
		Optional<Talla> OptTalla = tallaRepo.findByUuid(uuid);
		if (OptTalla.isPresent()) {
			return mapper.map(OptTalla.get(), TallaDTO.class);
		} else {
			throw new EntityNotFoundException("Talla no encontrada con el UUID: " + uuid);
		}
	}
}
