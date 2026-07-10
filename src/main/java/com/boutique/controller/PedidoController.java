package com.boutique.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boutique.dto.PedidoDTO;
import com.boutique.service.PedidoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/rutaPedidos")
public class PedidoController {

    @Autowired
    PedidoService pedidoService;

    @GetMapping("listar")
    public String metodoListar(Model model) {
        model.addAttribute("pedidos", pedidoService.listar());
        return "/carpetaPedidos/paginaPedidos";
    }

    @GetMapping("nuevo")
    public String metodoNuevo(Model model) {
        model.addAttribute("pedido", new PedidoDTO());
        model.addAttribute("uuid", null);
        return "/carpetaPedidos/paginaFormulario";
    }

    @PostMapping("guardar")
    public String metodoGuardar(@Valid @ModelAttribute("pedido") PedidoDTO pedido, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("uuid", null);
            return "/carpetaPedidos/paginaFormulario";
        }
        pedidoService.guardar(pedido);
        return "redirect:/rutaPedidos/listar";
    }

    @PostMapping("actualizar")
    public String metodoActualizar(@Valid @ModelAttribute("pedido") PedidoDTO pedido, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("uuid", pedido.getUuid());
            return "/carpetaPedidos/paginaFormulario";
        }
        pedidoService.actualizar(pedido);
        return "redirect:/rutaPedidos/listar";
    }

    @GetMapping("editar/{uuid}")
    public String metodoEditar(Model model, @PathVariable UUID uuid) {
        model.addAttribute("pedido", pedidoService.obtenerPedidoUUID(uuid));
        model.addAttribute("uuid", uuid);
        return "/carpetaPedidos/paginaFormulario";
    }

    @GetMapping("eliminar/{uuid}")
    public String metodoEliminar(Model model, @PathVariable UUID uuid) {
        pedidoService.borrar(uuid);
        return "redirect:/rutaPedidos/listar";
    }
}