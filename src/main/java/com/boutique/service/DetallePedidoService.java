package com.boutique.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutique.dto.DetallePedidoDTO;
import com.boutique.model.DetallePedido;
import com.boutique.model.DetalleProducto;
import com.boutique.model.Pedido;
import com.boutique.repo.DetallePedidoRepo;
import com.boutique.repo.DetalleProductoRepo;
import com.boutique.repo.PedidoRepo;

@Service
public class DetallePedidoService {
    @Autowired
    private DetallePedidoRepo detallePedidoRepo;
    
    @Autowired
    private PedidoRepo pedidoRepo;
    
    @Autowired
    private DetalleProductoRepo detalleProductoRepo;

    public List<DetallePedido> listar() {
        return detallePedidoRepo.findAll();
    }

    public void guardar(DetallePedidoDTO dto) {
        DetallePedido detalle;
        if (dto.getUuid() != null) {
            detalle = detallePedidoRepo.findByUuid(dto.getUuid())
                    .orElseThrow(() -> new IllegalArgumentException("Detalle de pedido no encontrado: " + dto.getUuid()));
        } else if (dto.getIdDetPed() != null) {
            detalle = detallePedidoRepo.findById(dto.getIdDetPed())
                    .orElse(new DetallePedido());
        } else {
            detalle = new DetallePedido();
        }
        
        if (dto.getIdPedido() == null) {
            throw new IllegalArgumentException("Debe elegir un pedido válido");
        }
        Pedido pedido = pedidoRepo.findById(dto.getIdPedido())
                .orElseThrow(() -> new IllegalArgumentException("Pedido no válido"));
        detalle.setPedido(pedido);

        DetalleProducto detProd = detalleProductoRepo.findById(dto.getIdDetProd())
                .orElseThrow(() -> new IllegalArgumentException("Detalle de producto no válido"));

        detalle.setDetalleProducto(detProd);
        detalle.setCantidad(dto.getCantidad());
        detalle.setPrecioUnitario(dto.getPrecioUnitario());
        
        if (dto.getCantidad() != null && dto.getPrecioUnitario() != null) {
            detalle.setSubtotal(dto.getCantidad() * dto.getPrecioUnitario());
        }
        
        detallePedidoRepo.save(detalle);
    }

    public DetallePedidoDTO obtenerDetallePedidoUUID(UUID uuid) {
        DetallePedido detalle = detallePedidoRepo.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Detalle de pedido no encontrado: " + uuid));

        DetallePedidoDTO dto = new DetallePedidoDTO();
        dto.setIdDetPed(detalle.getIdDetPed());
        dto.setUuid(detalle.getUuid());
        dto.setIdPedido(detalle.getPedido() != null ? detalle.getPedido().getIdPedido() : null);
        dto.setIdDetProd(detalle.getDetalleProducto() != null ? detalle.getDetalleProducto().getIdDetProd() : null);
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        return dto;
    }

    public void eliminar(UUID uuid) {
        DetallePedido detalle = detallePedidoRepo.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Detalle de pedido no encontrado: " + uuid));
        detallePedidoRepo.delete(detalle);
    }

}
