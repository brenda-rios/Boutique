package com.boutique.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.boutique.dto.VentaResumenDTO;
import com.boutique.dto.DetalleVentaDTO;
import com.boutique.enums.Estado;
import com.boutique.repo.CategoriaRepo;
import com.boutique.repo.ProductoRepo;
import com.boutique.repo.PedidoRepo;
import com.boutique.service.ReporteService;

@Controller
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private CategoriaRepo categoriaRepo;

    @Autowired
    private PedidoRepo pedidoRepo;

    @GetMapping("/reportes/ventas")
    public String listarVentas(@RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta,
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String estado,
            Model model) {

        LocalDate dDesde = null;
        LocalDate dHasta = null;
        if (desde != null && !desde.isBlank()) dDesde = LocalDate.parse(desde);
        if (hasta != null && !hasta.isBlank()) dHasta = LocalDate.parse(hasta);

        Estado e = null;
        if (estado != null && !estado.isBlank()) {
            try { e = Estado.valueOf(estado); } catch (Exception ex) { e = null; }
        }

        List<VentaResumenDTO> ventas = reporteService.getVentas(dDesde, dHasta, productoId, categoriaId, e);

        model.addAttribute("ventas", ventas);
        model.addAttribute("productos", productoRepo.findAll());
        model.addAttribute("categorias", categoriaRepo.findAll());
        model.addAttribute("estados", Estado.values());
        model.addAttribute("fDesde", desde);
        model.addAttribute("fHasta", hasta);
        model.addAttribute("fProductoId", productoId);
        model.addAttribute("fCategoriaId", categoriaId);
        model.addAttribute("fEstado", estado);

        return "reportes/ventas";
    }

    @GetMapping("/reportes/ventas/{uuid}")
    public String detalleVenta(@PathVariable String uuid, Model model) {
        List<DetalleVentaDTO> detalles = reporteService.getDetallesByPedidoUuid(UUID.fromString(uuid));
        model.addAttribute("detalles", detalles);
        model.addAttribute("pedidoUuid", uuid);
        pedidoRepo.findByUuid(UUID.fromString(uuid)).ifPresent(p -> model.addAttribute("pedido", p));
        return "reportes/ventaDetalle";
    }

}
