package com.boutique.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boutique.model.Talla;

public interface TallaRepo extends JpaRepository<Talla, Long> {

	Optional<Talla> findByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);

	
}
 