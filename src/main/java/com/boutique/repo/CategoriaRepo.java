package com.boutique.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boutique.model.Categoria;

import java.util.UUID;


public interface CategoriaRepo extends JpaRepository<Categoria, Long>{
	
Optional<Categoria>  findByUuid(UUID uuid);
Void deleteByUuid(UUID uuid);

}
