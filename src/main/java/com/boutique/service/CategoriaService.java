package com.boutique.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutique.dto.CategoriaDTO;
import com.boutique.model.Categoria;
import com.boutique.repo.CategoriaRepo;

import jakarta.persistence.EntityNotFoundException;

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
    
    public void actualizar(CategoriaDTO categoria) {
        Optional<Categoria> optCategoria = categoriaRepo.findByUuid(categoria.getUuid());
        
        if (optCategoria.isPresent()) {
            Categoria entidadExistente = optCategoria.get();
            
            // Mapeo manual: solo actualizamos los campos que el usuario edita
            entidadExistente.setNombre(categoria.getNombre());
            // Si tu categoría tiene otros campos editables (ej. descripcion), agrégalos aquí:
            // entidadExistente.setDescripcion(categoria.getDescripcion());
            
            // Guardamos la entidad original, manteniendo su ID y UUID intactos
            categoriaRepo.save(entidadExistente);
        } else {
            throw new EntityNotFoundException("Categoría no encontrada con UUID: " + categoria.getUuid());
        }
    }
	
	public void borrar(UUID uuid) {
		Optional<Categoria> OptCategoria = categoriaRepo.findByUuid(uuid);
		if (OptCategoria.isPresent()) {
			categoriaRepo.delete(OptCategoria.get());
		} else {
			throw new EntityNotFoundException("Categoría no encontrada con UUID: " + uuid);
		}
	}
	
	public CategoriaDTO obtenerCategoriaUUID(UUID uuid) {
		Optional<Categoria> OptCategoria = categoriaRepo.findByUuid(uuid);
		if (OptCategoria.isPresent()) {
			return mapper.map(OptCategoria.get(), CategoriaDTO.class);
		} else {
			throw new EntityNotFoundException("Categoría no encontrada con UUID: " + uuid);
		}
	}

}
