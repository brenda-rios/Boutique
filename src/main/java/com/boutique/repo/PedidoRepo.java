package com.boutique.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boutique.model.Pedido;

public interface PedidoRepo extends JpaRepository <Pedido, Long> {

	    Optional<Pedido> findByUuid(UUID uuid);
	    
	    void deleteByUuid(UUID uuid); 
	}
	
	
