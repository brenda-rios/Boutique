package com.boutique.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boutique.model.DetalleProducto;
import com.boutique.model.Color;
import com.boutique.model.Talla;
import com.boutique.model.Producto;

@Repository
public interface DetalleProductoRepo extends JpaRepository<DetalleProducto, Long> {
	Optional<DetalleProducto> findByUuid(UUID uuid);
	void deleteByUuid(UUID uuid);
	long countByColor(Color color);
	long countByTalla(Talla talla);
	long countByProducto(Producto producto);
	List<DetalleProducto> findByProducto(Producto producto);
	void deleteByProducto(Producto producto);
}
