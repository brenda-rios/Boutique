package com.boutique.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boutique.model.DetallePedido;
import com.boutique.model.Pedido;
import java.util.List;

@Repository
public interface DetallePedidoRepo extends JpaRepository<DetallePedido, Long> {
    Optional<DetallePedido> findByUuid(UUID uuid);
    List<DetallePedido> findByPedido(Pedido pedido);
}
