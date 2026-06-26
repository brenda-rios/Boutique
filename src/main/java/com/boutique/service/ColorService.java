package com.boutique.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutique.dto.ColorDTO;
import com.boutique.model.Color;
import com.boutique.repo.ColorRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ColorService {
	@Autowired
    ColorRepo colorRepo;
    @Autowired
    ModelMapper mapper;

    public List<ColorDTO> listar() {
        return colorRepo.findAll().stream()
                .map(color -> mapper.map(color, ColorDTO.class))
                .toList();
    }

    public void guardar(ColorDTO color) {
        colorRepo.save(mapper.map(color, Color.class));
    }
    
    public void actualizar(ColorDTO color) {
		Optional<Color> OptColor = colorRepo.findByUuid(color.getUuid());
		if (OptColor.isPresent()) {
			mapper.map(color, OptColor.get());
			colorRepo.save(OptColor.get());
		} else {
			throw new EntityNotFoundException("Color no encontrado con el UUID: " + color.getUuid());
		}	
	}
	
	public void borrar(UUID uuid) {
		Optional<Color> OptColor = colorRepo.findByUuid(uuid);
		if (OptColor.isPresent()) {
			colorRepo.delete(OptColor.get());
		} else {
			throw new EntityNotFoundException("Color no encontrado con el UUID: " + uuid);
		}
	}
	
	public ColorDTO obtenerColorUUID(UUID uuid) {
		Optional<Color> OptColor = colorRepo.findByUuid(uuid);
		if (OptColor.isPresent()) {
			return mapper.map(OptColor.get(), ColorDTO.class);
		} else {
			throw new EntityNotFoundException("Color no encontrado con el UUID: " + uuid);
		}
	}
}
