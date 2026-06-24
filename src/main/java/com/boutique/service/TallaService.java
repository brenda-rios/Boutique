package com.boutique.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutique.dto.TallaDTO;
import com.boutique.model.Talla;
import com.boutique.repo.TallaRepo;

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

}
