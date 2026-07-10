package com.boutique.service;

import java.time.LocalDateTime;
import java.util.Arrays; 
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutique.dto.PedidoDTO;
import com.boutique.model.Pedido;
import com.boutique.repo.PedidoRepo;
import com.boutique.repo.DetallePedidoRepo;
import com.boutique.exception.ReferencedEntityException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PedidoService {

    @Autowired
    PedidoRepo pedidoRepo;
    
    @Autowired
    private DetallePedidoRepo detallePedidoRepo;
    
    @Autowired
    ModelMapper mapper;

    public List<PedidoDTO> listar() {
        return pedidoRepo.findAll().stream()
                .map(pedido -> {
                    PedidoDTO dto = mapper.map(pedido, PedidoDTO.class);
                    dto.setIntegranteSeleccionado(obtenerIntegranteSeleccionado(pedido));
                    return dto;
                })
                .toList();
    }

    // 💡 Lógica mejorada idéntica a productos para autogenerar datos faltantes
    public void guardar(PedidoDTO pedidoDTO) {
        Pedido pedido = mapper.map(pedidoDTO, Pedido.class);
        
        if (pedido.getUuid() == null) {
            pedido.setUuid(UUID.randomUUID());
        }
        if (pedido.getFechaHora() == null) {
            pedido.setFechaHora(LocalDateTime.now());
        }
        // 🔴 INICIALIZAMOS EL TOTAL EN 0 PARA PEDIDOS NUEVOS
        if (pedido.getTotal() == null) {
            pedido.setTotal(0f);
        }
        
        pedidoRepo.save(pedido);
    }

    public void actualizar(PedidoDTO pedido) {
        Optional<Pedido> OptPedido = pedidoRepo.findByUuid(pedido.getUuid());
        if (OptPedido.isPresent()) {
            Pedido entidad = OptPedido.get();
            // Recalcular total a partir de los detalles asociados (no usar el valor pasado en el DTO)
            float suma = 0f;
            java.util.List<com.boutique.model.DetallePedido> detalles = detallePedidoRepo.findByPedido(entidad);
            if (detalles != null) {
                for (com.boutique.model.DetallePedido dp : detalles) {
                    if (dp.getSubtotal() != null) suma += dp.getSubtotal();
                }
            }
            entidad.setTotal(suma);
            entidad.setEstado(pedido.getEstado());
            entidad.setBrendaRV(pedido.getBrendaRV());
            entidad.setJoseArmandoBM(pedido.getJoseArmandoBM());
            entidad.setLizbethCL(pedido.getLizbethCL());
            entidad.setMairaPE(pedido.getMairaPE());
            pedidoRepo.save(entidad);
        } else {
            throw new EntityNotFoundException("Pedido no encontrado con el UUID: " + pedido.getUuid());
        }
    }

    public void borrar(UUID uuid) {
        Optional<Pedido> OptPedido = pedidoRepo.findByUuid(uuid);
        if (OptPedido.isPresent()) {
            Pedido pedido = OptPedido.get();
            // Validar que no hay detalles de pedido asociados
            long detallesPedidoCount = detallePedidoRepo.countByPedido(pedido);
            if (detallesPedidoCount > 0) {
                throw new ReferencedEntityException(
                    "No se puede eliminar este pedido porque tiene " + detallesPedidoCount + " detalle(s) asociado(s)."
                );
            }
            pedidoRepo.delete(pedido);
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

    private String obtenerIntegranteSeleccionado(Pedido pedido) {
        // 🛠️ Cambiado List.of por Arrays.asList para soportar campos nulos (null) de forma segura
        List<String> integrantes = Arrays.asList(
        		"Brenda: " + pedido.getBrendaRV(),
        		"Armando: " + pedido.getJoseArmandoBM(),
        		"Lizbeth: " + pedido.getLizbethCL(),
        		"Maira: " + pedido.getMairaPE());
                
        List<String> valoresValidos = integrantes.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
                
        if (valoresValidos.isEmpty()) {
            return "Sin asignar"; // O dejarlo vacío "" según prefieras
        }
        
        int indice = (int) (Math.random() * valoresValidos.size());
        return valoresValidos.get(indice);
    }
}