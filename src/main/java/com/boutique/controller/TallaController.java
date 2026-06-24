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

import com.boutique.dto.TallaDTO;
import com.boutique.service.TallaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("rutaTallas")
public class TallaController {
	@Autowired
    TallaService tallaService;

    @GetMapping("listar")
    public String metodoListar(Model model) {
        model.addAttribute("tallas", tallaService.listar());
        return "/carpetaTallas/paginaTallas";
    }

    @GetMapping("nuevo")
    public String metodoNuevo(Model model) {
        model.addAttribute("talla", new TallaDTO());
        return "/carpetaTallas/paginaFormulario";
    }

    @PostMapping("guardar")
    public String metodoGuardar(@Valid @ModelAttribute("talla") TallaDTO talla, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/carpetaTallas/paginaFormulario";
        }
        tallaService.guardar(talla);
        return "redirect:/rutaTallas/listar";
    }
    @PostMapping("actualizar")
	public String metodoActualizar(@Valid @ModelAttribute("talla") TallaDTO talla, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("uuid", talla.getUuid());
			return "/carpetaTallas/paginaFormulario";
		}
		tallaService.actualizar(talla);
		return "redirect:/rutaTallas/listar";
	}
	
	@GetMapping("editar/{uuid}")
	public String metodoEditar(Model model, @PathVariable UUID uuid) {
		model.addAttribute("talla", tallaService.obtenerTallaUUID(uuid));
		model.addAttribute("uuid", uuid);
		return "/carpetaTallas/paginaFormulario";
	}
	
	@GetMapping("eliminar/{uuid}")
	public String metodoEliminar(Model model, @PathVariable UUID uuid) {
		tallaService.borrar(uuid);
		return "redirect:/rutaTallas/listar";
	}

	}
