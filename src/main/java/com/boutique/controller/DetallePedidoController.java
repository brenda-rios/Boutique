package com.boutique.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boutique.dto.DetallePedidoDTO;
import com.boutique.service.DetallePedidoService;
import com.boutique.service.DetalleProductoService;

@Controller
@RequestMapping("/detallePedido")
public class DetallePedidoController {
	@Autowired private DetallePedidoService detallePedidoService;
    //@Autowired private PedidoService pedidoService; 
    @Autowired private DetalleProductoService detalleProductoService;

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("detalles", detallePedidoService.listar());
        return "carpetaDetallePedidos/paginaDetallePedidos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("detallePedido", new DetallePedidoDTO());
        // Enviamos las listas para llenar los <select>
        //model.addAttribute("listaPedidos", pedidoService.listar());
        model.addAttribute("listaDetallesProd", detalleProductoService.listar());
        return "carpetaDetallePedidos/paginaFormulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute DetallePedidoDTO detallePedido) {
        detallePedidoService.guardar(detallePedido);
        return "redirect:/detallePedido/listar";
    }

}
