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

import jakarta.persistence.EntityNotFoundException;

@Service
public class TallaService {
	@Autowired
    TallaRepo tallaRepo;
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
			mapper.map(talla, OptTalla.get());
			tallaRepo.save(OptTalla.get());
		} else {
			throw new EntityNotFoundException("Talla no encontrada con el UUID: " + talla.getUuid());
		}	
	}
	
	public void borrar(UUID uuid) {
		Optional<Talla> OptTalla = tallaRepo.findByUuid(uuid);
		if (OptTalla.isPresent()) {
			tallaRepo.delete(OptTalla.get());
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
