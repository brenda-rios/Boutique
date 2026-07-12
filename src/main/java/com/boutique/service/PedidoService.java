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

import org.springframework.transaction.annotation.Transactional;

import com.boutique.dto.PedidoDTO;
import com.boutique.dto.DetallePedidoDTO;
import com.boutique.model.Pedido;
import com.boutique.model.DetallePedido;
import com.boutique.model.DetalleProducto;
import com.boutique.repo.PedidoRepo;
import com.boutique.repo.DetallePedidoRepo;
import com.boutique.repo.DetalleProductoRepo;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PedidoService {

    @Autowired
    PedidoRepo pedidoRepo;
    
    @Autowired
    private DetallePedidoRepo detallePedidoRepo;
    
    @Autowired
    private DetalleProductoRepo detalleProductoRepo;
    
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
    @Transactional
    public void guardar(PedidoDTO pedidoDTO) {
        Pedido pedido = mapper.map(pedidoDTO, Pedido.class);
        
        if (pedido.getUuid() == null) {
            pedido.setUuid(UUID.randomUUID());
        }
        if (pedido.getFechaHora() == null) {
            pedido.setFechaHora(LocalDateTime.now());
        }
        
        pedido = pedidoRepo.save(pedido);

        float total = 0f;
        if (pedidoDTO.getDetalles() != null) {
            for (DetallePedidoDTO detDto : pedidoDTO.getDetalles()) {
                DetallePedido det = new DetallePedido();
                det.setPedido(pedido);
                det.setCantidad(detDto.getCantidad());
                
                DetalleProducto prod = detalleProductoRepo.findById(detDto.getIdDetProd())
                        .orElseThrow(() -> new IllegalArgumentException("DetalleProducto no válido"));
                det.setDetalleProducto(prod);
                
                // Si el front no manda el precio, lo sacamos del catálogo o usamos el del DTO si lo enviaron (y confiamos).
                // Mejor tomarlo directo del producto para evitar alteraciones de precio desde el frontend.
                Float precioUnit = prod.getPrecio();
                det.setPrecioUnitario(precioUnit);
                
                Float sub = precioUnit * det.getCantidad();
                det.setSubtotal(sub);
                
                total += sub;
                
                detallePedidoRepo.save(det);
            }
        }
        
        pedido.setTotal(total);
        pedidoRepo.save(pedido);
    }

    @Transactional
    public void actualizar(PedidoDTO pedido) {
        Optional<Pedido> OptPedido = pedidoRepo.findByUuid(pedido.getUuid());
        if (OptPedido.isPresent()) {
            Pedido entidad = OptPedido.get();
            
            entidad.setEstado(pedido.getEstado());
            entidad.setBrendaRV(pedido.getBrendaRV());
            entidad.setJoseArmandoBM(pedido.getJoseArmandoBM());
            entidad.setLizbethCL(pedido.getLizbethCL());
            entidad.setMairaPE(pedido.getMairaPE());
            
            entidad = pedidoRepo.save(entidad);
            
            // Recreamos los detalles
            List<DetallePedido> actuales = detallePedidoRepo.findByPedido(entidad);
            if (actuales != null) {
                detallePedidoRepo.deleteAll(actuales);
            }
            
            float total = 0f;
            if (pedido.getDetalles() != null) {
                for (DetallePedidoDTO detDto : pedido.getDetalles()) {
                    DetallePedido det = new DetallePedido();
                    det.setPedido(entidad);
                    det.setCantidad(detDto.getCantidad());
                    
                    DetalleProducto prod = detalleProductoRepo.findById(detDto.getIdDetProd())
                            .orElseThrow(() -> new IllegalArgumentException("DetalleProducto no válido"));
                    det.setDetalleProducto(prod);
                    
                    Float precioUnit = prod.getPrecio();
                    det.setPrecioUnitario(precioUnit);
                    
                    Float sub = precioUnit * det.getCantidad();
                    det.setSubtotal(sub);
                    
                    total += sub;
                    
                    detallePedidoRepo.save(det);
                }
            }
            
            entidad.setTotal(total);
            pedidoRepo.save(entidad);
        } else {
            throw new EntityNotFoundException("Pedido no encontrado con el UUID: " + pedido.getUuid());
        }
    }

    @Transactional
    public void borrar(UUID uuid) {
        Optional<Pedido> OptPedido = pedidoRepo.findByUuid(uuid);
        if (OptPedido.isPresent()) {
            Pedido pedido = OptPedido.get();
            detallePedidoRepo.deleteByPedido(pedido);
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