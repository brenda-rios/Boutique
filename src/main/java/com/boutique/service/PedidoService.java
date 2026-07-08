package com.boutique.service;

	import java.time.LocalDateTime;
import java.util.List;
	import java.util.Optional;
	import java.util.UUID;

	import org.modelmapper.ModelMapper;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;

	import com.boutique.dto.PedidoDTO;
	import com.boutique.model.Pedido;
	import com.boutique.repo.PedidoRepo;

	import jakarta.persistence.EntityNotFoundException;

	@Service
	public class PedidoService {

	    @Autowired
	    PedidoRepo pedidoRepo;
	    
	    @Autowired
	    ModelMapper mapper;

	    public List<PedidoDTO> listar() {
	        return pedidoRepo.findAll().stream()
	                .map(pedido -> mapper.map(pedido, PedidoDTO.class))
	                .toList();
	    }

	    public void guardar(PedidoDTO pedido) {
	        pedidoRepo.save(mapper.map(pedido, Pedido.class));
	    }

	    public void actualizar(PedidoDTO pedido) {
	        Optional<Pedido> OptPedido = pedidoRepo.findByUuid(pedido.getUuid());
	        if (OptPedido.isPresent()) {
	            Pedido entidad = OptPedido.get();
	           
	            entidad.setTotal(pedido.getTotal());
	            entidad.setEstado(pedido.getEstado());
	          
	            pedidoRepo.save(entidad);
	        } else {
	            throw new EntityNotFoundException("Pedido no encontrado con el UUID: " + pedido.getUuid());
	        }
	    }

	    public void borrar(UUID uuid) {
	        Optional<Pedido> OptPedido = pedidoRepo.findByUuid(uuid);
	        if (OptPedido.isPresent()) {
	            pedidoRepo.delete(OptPedido.get());
	        } else {
	            throw new EntityNotFoundException("Pedido no encontrado con UUID: " + uuid);
	        }
	    }

	    public PedidoDTO obtenerPedidoUUID(UUID uuid) {
	        Optional<Pedido> OptPedido = pedidoRepo.findByUuid(uuid);
	        if (OptPedido.isPresent()) {
	            return mapper.map(OptPedido.get(), PedidoDTO.class);
	        } else {
	            throw new EntityNotFoundException("Pedido no encontrado con UUID: " + uuid);
	        }
	    }
	}
	

