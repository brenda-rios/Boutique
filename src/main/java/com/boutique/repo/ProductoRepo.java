package com.boutique.repo;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.boutique.model.Producto;
import com.boutique.model.Categoria;

public interface ProductoRepo extends JpaRepository<Producto, Long> {
	Optional<Producto> findByUuid(UUID uuid);
	long countByCategoria(Categoria categoria);
}