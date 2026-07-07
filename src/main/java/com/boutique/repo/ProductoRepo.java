package com.boutique.repo;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.boutique.model.Producto;

public interface ProductoRepo extends JpaRepository<Producto, Long> {
	Optional<Producto> findByUuid(UUID uuid);
}