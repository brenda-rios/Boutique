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

import com.boutique.dto.ColorDTO;
import com.boutique.service.ColorService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/rutaColores")
public class ColorController {
	@Autowired
    ColorService colorService;

    @GetMapping("/listar")
    public String metodoListar(Model model) {
        model.addAttribute("colores", colorService.listar());
        return "carpetaColores/paginaColores";
    }

    @GetMapping("/nuevo")
    public String metodoNuevo(Model model) {
        model.addAttribute("color", new ColorDTO()); // El DTO vacío ya viaja con su uuid en null por defecto
        return "/carpetaColores/paginaFormulario";
    }

    @PostMapping("/guardar")
    public String metodoGuardar(@Valid @ModelAttribute("color") ColorDTO color, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "carpetaColores/paginaFormulario";
        }
        colorService.guardar(color);
        return "redirect:/rutaColores/listar"; // CORRECCIÓN: Barra "/" añadida al redirect
    }
    
    @PostMapping("/actualizar")
	public String metodoActualizar(@Valid @ModelAttribute("color") ColorDTO color, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "carpetaColores/paginaFormulario"; // CORRECCIÓN: Ya no necesitas setear el uuid en el modelo individualmente
		}
		colorService.actualizar(color);
		return "redirect:/rutaColores/listar"; // CORRECCIÓN: Barra "/" añadida al redirect
	}
	
	@GetMapping("/editar/{uuid}")
	public String metodoEditar(Model model, @PathVariable UUID uuid) {
		model.addAttribute("color", colorService.obtenerColorUUID(uuid)); // El DTO recuperado ya trae su UUID dentro
		return "/carpetaColores/paginaFormulario";
	}
	
	@GetMapping("/eliminar/{uuid}")
	public String metodoEliminar(Model model, @PathVariable UUID uuid) {
		colorService.borrar(uuid);
		return "redirect:/rutaColores/listar"; // CORRECCIÓN: Barra "/" añadida al redirect
	}
}
