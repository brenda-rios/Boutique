package com.boutique.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
}
