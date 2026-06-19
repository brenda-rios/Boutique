package com.boutique.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boutique.model.DetalleProducto;

@Repository
public interface DetalleProductoRepo extends JpaRepository<DetalleProducto, Long> {
	Optional<DetalleProducto> findByUuid(UUID uuid);
	void deleteByUuid(UUID uuid);
}
