package com.boutique.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.boutique.dto.VentaResumenDTO;
import com.boutique.dto.DetalleVentaDTO;
import com.boutique.enums.Estado;

public interface ReporteService {
    List<VentaResumenDTO> getVentas(LocalDate desde, LocalDate hasta, Long productoId, Long categoriaId, Estado estado);

    List<DetalleVentaDTO> getDetallesByPedidoUuid(UUID uuid);
}
