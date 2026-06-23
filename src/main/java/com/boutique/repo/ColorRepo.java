package com.boutique.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boutique.model.Color;

@Repository
public interface ColorRepo extends JpaRepository<Color, Long> {
    Optional<Color> findByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);
}
