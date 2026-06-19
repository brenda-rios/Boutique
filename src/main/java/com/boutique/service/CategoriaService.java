package com.boutique.service;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutique.dto.CategoriaDTO;
import com.boutique.model.Categoria;
import com.boutique.repo.CategoriaRepo;

@Service
public class CategoriaService {
	
	@Autowired
    CategoriaRepo categoriaRepo;
    @Autowired
    ModelMapper mapper;

    public List<CategoriaDTO> listar() {
        return categoriaRepo.findAll().stream()
                .map(categoria -> mapper.map(categoria, CategoriaDTO.class))
                .toList();
    }

    public void guardar(CategoriaDTO categoria) {
        categoriaRepo.save(mapper.map(categoria, Categoria.class));
    }

}
