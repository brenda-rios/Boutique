package com.boutique.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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

        // Para evitar productos repetidos
        Set<Long> idsProcesados = new HashSet<>();

        if (pedidoDTO.getDetalles() != null) {

            for (DetallePedidoDTO detDto : pedidoDTO.getDetalles()) {

                // Validar productos duplicados
                if (!idsProcesados.add(detDto.getIdDetProd())) {
                    throw new IllegalArgumentException("El producto ya está incluido en este pedido.");
                }

                // Validar cantidad
                if (detDto.getCantidad() <= 0) {
                    throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
                }

                DetallePedido det = new DetallePedido();
                det.setPedido(pedido);
                det.setCantidad(detDto.getCantidad());

                DetalleProducto prod = detalleProductoRepo.findById(detDto.getIdDetProd())
                        .orElseThrow(() -> new IllegalArgumentException("DetalleProducto no válido."));

                // Validar stock
                if (prod.getCantidad() < detDto.getCantidad()) {
                    throw new IllegalArgumentException("Stock insuficiente para: " + prod.getProducto().getNombre());
                }

                // Descontar stock
                prod.setCantidad(prod.getCantidad() - detDto.getCantidad());
                detalleProductoRepo.save(prod);

                det.setDetalleProducto(prod);

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

            // 1. REGRESAR STOCK DE LOS DETALLES VIEJOS
            List<DetallePedido> actuales = detallePedidoRepo.findByPedido(entidad);

            if (actuales != null && !actuales.isEmpty()) {

                // Mantengo exactamente tu lógica original
                detallePedidoRepo.deleteAll(actuales);

                for (DetallePedido det : actuales) {

                    DetalleProducto prod = det.getDetalleProducto();

                    DetalleProducto prodActualizado = detalleProductoRepo
                            .findById(prod.getIdDetProd())
                            .orElse(prod);

                    prodActualizado.setCantidad(
                            prodActualizado.getCantidad() + det.getCantidad());

                    detalleProductoRepo.save(prodActualizado);
                }
            }

            // 2. ACTUALIZAR DATOS DEL PEDIDO
            entidad.setEstado(pedido.getEstado());
            entidad.setBrendaRV(pedido.getBrendaRV());
            entidad.setJoseArmandoBM(pedido.getJoseArmandoBM());
            entidad.setLizbethCL(pedido.getLizbethCL());
            entidad.setMairaPE(pedido.getMairaPE());

            entidad = pedidoRepo.save(entidad);

            float total = 0f;

            // Evitar productos repetidos
            Set<Long> idsProcesados = new HashSet<>();

            if (pedido.getDetalles() != null) {

                for (DetallePedidoDTO detDto : pedido.getDetalles()) {

                    // Validar duplicados
                    if (!idsProcesados.add(detDto.getIdDetProd())) {
                        throw new IllegalArgumentException("El producto ya está incluido en este pedido.");
                    }

                    // Validar cantidad
                    if (detDto.getCantidad() <= 0) {
                        throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
                    }

                    DetallePedido det = new DetallePedido();
                    det.setPedido(entidad);
                    det.setCantidad(detDto.getCantidad());

                    DetalleProducto prod = detalleProductoRepo.findById(detDto.getIdDetProd())
                            .orElseThrow(() -> new IllegalArgumentException("DetalleProducto no válido."));

                    // Validar stock
                    if (prod.getCantidad() < detDto.getCantidad()) {
                        throw new IllegalArgumentException("Stock insuficiente para: " + prod.getProducto().getNombre());
                    }

                    // Descontar stock
                    prod.setCantidad(prod.getCantidad() - detDto.getCantidad());
                    detalleProductoRepo.save(prod);

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
            throw new EntityNotFoundException(
                    "Pedido no encontrado con el UUID: " + pedido.getUuid());
        }
    }

    @Transactional
    public void borrar(UUID uuid) {
        Optional<Pedido> OptPedido = pedidoRepo.findByUuid(uuid);
        if (OptPedido.isPresent()) {
            Pedido pedido = OptPedido.get();
            
            
         // --- Manejo de cantidades ---
            List<DetallePedido> detalles = detallePedidoRepo.findByPedido(pedido);
            for (DetallePedido det : detalles) {
                DetalleProducto prod = det.getDetalleProducto();
                prod.setCantidad(prod.getCantidad() + det.getCantidad()); // Regresamos stock
                detalleProductoRepo.save(prod);
            }
            // ------------------------------------
            
            
            
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