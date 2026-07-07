package com.boutique.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutique.dto.DetallePedidoDTO;
import com.boutique.model.DetallePedido;
import com.boutique.repo.DetallePedidoRepo;
import com.boutique.repo.DetalleProductoRepo;

@Service
public class DetallePedidoService {
	@Autowired
    private DetallePedidoRepo detallePedidoRepo;
    
    /*@Autowired
    private PedidoRepo pedidoRepo;*/
    
    @Autowired
    private DetalleProductoRepo detalleProductoRepo;

    public List<DetallePedido> listar() {
        return detallePedidoRepo.findAll();
    }

    public void guardar(DetallePedidoDTO dto) {
        DetallePedido detalle = new DetallePedido();
        
        /*if (dto.getIdDetPed() != null) {
            detalle.setIdDetPed(dto.getIdDetPed());
        }
        
        // Buscamos los objetos reales usando los repositorios
        Pedido pedido = pedidoRepo.findById(dto.getIdPedido()).orElse(null);
        DetalleProducto detProd = detalleProductoRepo.findById(dto.getIdDetProd()).orElse(null);
        
        detalle.setPedido(pedido);
        detalle.setDetalleProducto(detProd);
        detalle.setCantidad(dto.getCantidad());
        detalle.setPrecioUnitario(dto.getPrecioUnitario());
        
        // Calculamos el subtotal de manera automática
        if(dto.getCantidad() != null && dto.getPrecioUnitario() != null){
            detalle.setSubtotal(dto.getCantidad() * dto.getPrecioUnitario());
        }

        detallePedidoRepo.save(detalle);*/
    }

}
