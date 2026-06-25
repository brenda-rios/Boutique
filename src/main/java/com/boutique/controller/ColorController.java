package com.boutique.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boutique.dto.ColorDTO;
import com.boutique.service.ColorService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("rutaColores")
public class ColorController {
	@Autowired
    ColorService colorService;

    @GetMapping("listar")
    public String metodoListar(Model model) {
        model.addAttribute("colores", colorService.listar());
        return "/carpetaColores/paginaColores";
    }

    @GetMapping("nuevo")
    public String metodoNuevo(Model model) {
        model.addAttribute("color", new ColorDTO());
        return "/carpetaColores/paginaFormulario";
    }

    @PostMapping("guardar")
    public String metodoGuardar(@Valid @ModelAttribute("color") ColorDTO color, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/carpetaColores/paginaFormulario";
        }
        colorService.guardar(color);
        return "redirect:/rutaColores/listar";
    }
}
