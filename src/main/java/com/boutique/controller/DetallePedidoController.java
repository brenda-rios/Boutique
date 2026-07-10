package com.boutique.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boutique.dto.DetallePedidoDTO;
import com.boutique.service.DetallePedidoService;
import com.boutique.service.DetalleProductoService;
import com.boutique.service.PedidoService;

@Controller
@RequestMapping("/rutaDetallePedido")
public class DetallePedidoController {
    @Autowired private DetallePedidoService detallePedidoService;
    @Autowired private PedidoService pedidoService;
    @Autowired private DetalleProductoService detalleProductoService;

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("detalles", detallePedidoService.listar());
        return "carpetaDetallePedidos/paginaDetallePedidos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("detallePedido", new DetallePedidoDTO());
        model.addAttribute("listaPedidos", pedidoService.listar());
        model.addAttribute("listaDetallesProd", detalleProductoService.listar());
        return "carpetaDetallePedidos/paginaFormulario";
    }

    @GetMapping("/editar/{uuid}")
    public String editar(@PathVariable UUID uuid, Model model) {
        model.addAttribute("detallePedido", detallePedidoService.obtenerDetallePedidoUUID(uuid));
        model.addAttribute("listaPedidos", pedidoService.listar());
        model.addAttribute("listaDetallesProd", detalleProductoService.listar());
        return "carpetaDetallePedidos/paginaFormulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute DetallePedidoDTO detallePedido) {
        detallePedidoService.guardar(detallePedido);
        return "redirect:/rutaDetallePedido/listar";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute DetallePedidoDTO detallePedido) {
        detallePedidoService.guardar(detallePedido);
        return "redirect:/rutaDetallePedido/listar";
    }

    @GetMapping("/eliminar/{uuid}")
    public String eliminar(@PathVariable UUID uuid) {
        detallePedidoService.eliminar(uuid);
        return "redirect:/rutaDetallePedido/listar";
    }
}
