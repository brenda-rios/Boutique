package com.boutique.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.boutique.dto.CategoriaDTO;
import com.boutique.service.CategoriaService;

import jakarta.validation.Valid;

public class CategoriaController {

	@Autowired
    CategoriaService categoriaService;

    @GetMapping("listar")
    public String metodoListar(Model model) {
        model.addAttribute("categorias", categoriaService.listar());
        return "/carpetaCategorias/paginaCategorias";
    }

    @GetMapping("nuevo")
    public String metodoNuevo(Model model) {
        model.addAttribute("categoria", new CategoriaDTO());
        return "/carpetaCategorias/paginaFormulario";
    }

    @PostMapping("guardar")
    public String metodoGuardar(@Valid @ModelAttribute("categoria") CategoriaDTO categoria, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/carpetaCategorias/paginaFormulario";
        }
        categoriaService.guardar(categoria);
        return "redirect:/rutaCategorias/listar";
    }
    
    @PostMapping("actualizar")
	public String metodoActualizar(@Valid @ModelAttribute("categoria") CategoriaDTO categoria, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("uuid", categoria.getUuid());
			return "/carpetaCategorias/paginaFormulario";
		}
		categoriaService.actualizar(categoria);
		return "redirect:/rutaCategorias/listar";
	}
	
	@GetMapping("editar/{uuid}")
	public String metodoEditar(Model model, @PathVariable UUID uuid) {
		model.addAttribute("categoria", categoriaService.obtenerCategoriaUUID(uuid));
		model.addAttribute("uuid", uuid);
		return "/carpetaCategorias/paginaFormulario";
	}
	
	@GetMapping("eliminar/{uuid}")
	public String metodoEliminar(Model model, @PathVariable UUID uuid) {
		categoriaService.borrar(uuid);
		return "redirect:/rutaCategorias/listar";
	}
}
