package com.boutique.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.boutique.model.Producto;
import com.boutique.model.Categoria;

public interface ProductoRepo extends JpaRepository<Producto, Long> {
	Optional<Producto> findByUuid(UUID uuid);
	long countByCategoria(Categoria categoria);
	// En tu ProductoRepo.java
	List<Producto> findByCategoriaIdCategoria(Long idCategoria);
}