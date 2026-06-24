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

import com.boutique.dto.DetalleProductoDTO;
import com.boutique.service.DetalleProductoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("rutaDetalleProductos")
public class DetalleProductoController {
    @Autowired
    DetalleProductoService detalleProductoService;

    @GetMapping("listar")
    public String metodoListar(Model model) {
        model.addAttribute("detalles", detalleProductoService.listar());
        return "/carpetaDetalles/paginaDetalles";
    }

    @GetMapping("nuevo")
    public String metodoNuevo(Model model) {
        model.addAttribute("detalleProducto", new DetalleProductoDTO());
        return "/carpetaDetalles/paginaFormulario";
    }

    @PostMapping("guardar")
    public String metodoGuardar(@Valid @ModelAttribute("detalleProducto") DetalleProductoDTO detalleProducto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/carpetaDetalles/paginaFormulario";
        }
        detalleProductoService.guardar(detalleProducto);
        return "redirect:/rutaDetalleProductos/listar";
    }
    @PostMapping("actualizar")
	public String metodoActualizar(@Valid @ModelAttribute("detalleProducto") DetalleProductoDTO detalle, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("uuid", detalle.getUuid());
			return "/carpetaDetalles/paginaFormulario";
		}
		detalleProductoService.actualizar(detalle);
		return "redirect:/rutaDetalleProductos/listar";
	}
	
	@GetMapping("editar/{uuid}")
	public String metodoEditar(Model model, @PathVariable UUID uuid) {
		model.addAttribute("detalleProducto", detalleProductoService.obtenerDetalleProductoUUID(uuid));
		model.addAttribute("uuid", uuid);
		return "/carpetaDetalles/paginaFormulario";
	}
	
	@GetMapping("eliminar/{uuid}")
	public String metodoEliminar(Model model, @PathVariable UUID uuid) {
		detalleProductoService.borrar(uuid);
		return "redirect:/rutaDetalleProductos/listar";
	}
}


