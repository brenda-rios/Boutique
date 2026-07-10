package com.boutique.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boutique.dto.DetalleVentaDTO;
import com.boutique.dto.VentaResumenDTO;
import com.boutique.enums.Estado;
import com.boutique.model.DetallePedido;
import com.boutique.model.DetalleProducto;
import com.boutique.model.Pedido;
import com.boutique.model.Producto;
import com.boutique.repo.DetallePedidoRepo;
import com.boutique.repo.PedidoRepo;

@Service
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private PedidoRepo pedidoRepo;

    @Autowired
    private DetallePedidoRepo detallePedidoRepo;

    @Override
    public List<VentaResumenDTO> getVentas(LocalDate desde, LocalDate hasta, Long productoId, Long categoriaId, Estado estado) {
        List<VentaResumenDTO> result = new ArrayList<>();
        List<Pedido> pedidos = pedidoRepo.findAll();

        for (Pedido p : pedidos) {
            if (p.getFechaHora() == null) continue;

            if (desde != null) {
                if (p.getFechaHora().toLocalDate().isBefore(desde)) continue;
            }
            if (hasta != null) {
                if (p.getFechaHora().toLocalDate().isAfter(hasta)) continue;
            }
            if (estado != null) {
                if (p.getEstado() == null || !p.getEstado().equals(estado)) continue;
            }

            List<DetallePedido> detalles = detallePedidoRepo.findByPedido(p);

            // Si hay filtro por producto o categoria, verificar que alguna linea cumpla
            if (productoId != null || categoriaId != null) {
                boolean cumple = false;
                for (DetallePedido dp : detalles) {
                    DetalleProducto detProd = dp.getDetalleProducto();
                    if (detProd == null) continue;
                    Producto prod = detProd.getProducto();
                    if (prod == null) continue;
                    if (productoId != null && prod.getIdProducto() != null && prod.getIdProducto().equals(productoId)) {
                        cumple = true; break;
                    }
                    if (categoriaId != null && prod.getCategoria() != null && prod.getCategoria().getIdCategoria() != null && prod.getCategoria().getIdCategoria().equals(categoriaId)) {
                        cumple = true; break;
                    }
                }
                if (!cumple) continue;
            }

            int nLineas = detalles == null ? 0 : detalles.size();
            String integrante = p.getIntegranteSeleccionado();
            VentaResumenDTO dto = new VentaResumenDTO(p.getUuid(), p.getFechaHora(), p.getTotal(), p.getEstado(), integrante, nLineas);
            result.add(dto);
        }

        return result;
    }

    @Override
    public List<DetalleVentaDTO> getDetallesByPedidoUuid(UUID uuid) {
        List<DetalleVentaDTO> lista = new ArrayList<>();
        Optional<Pedido> opt = pedidoRepo.findByUuid(uuid);
        if (opt.isEmpty()) return lista;
        Pedido pedido = opt.get();
        List<DetallePedido> detalles = detallePedidoRepo.findByPedido(pedido);
        if (detalles == null) return lista;

        for (DetallePedido dp : detalles) {
            DetalleProducto dpProd = dp.getDetalleProducto();
            String productoNombre = null;
            String categoriaNombre = null;
            String color = null;
            String talla = null;
            if (dpProd != null) {
                if (dpProd.getProducto() != null) {
                    productoNombre = dpProd.getProducto().getNombre();
                    if (dpProd.getProducto().getCategoria() != null) {
                        categoriaNombre = dpProd.getProducto().getCategoria().getNombre();
                    }
                }
                if (dpProd.getColor() != null) color = dpProd.getColor().getNombre();
                if (dpProd.getTalla() != null) talla = dpProd.getTalla().getNombre();
            }

            DetalleVentaDTO dto = new DetalleVentaDTO(productoNombre, categoriaNombre, color, talla, dp.getCantidad(), dp.getPrecioUnitario(), dp.getSubtotal());
            lista.add(dto);
        }

        return lista;
    }

}
