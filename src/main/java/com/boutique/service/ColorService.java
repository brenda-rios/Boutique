package com.boutique.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutique.dto.ColorDTO;
import com.boutique.model.Color;
import com.boutique.repo.ColorRepo;

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
}
