package com.boutique.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boutique.model.DetallePedido;

@Repository
public interface DetallePedidoRepo extends JpaRepository<DetallePedido, Long> {

}
