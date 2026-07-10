package com.boutique.controller;

import java.util.List;
import java.util.Objects;
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

import com.boutique.dto.ProductoDTO;
import com.boutique.model.Producto;
import com.boutique.service.CategoriaService;
import com.boutique.service.ProductoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/rutaProductos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("listar")
    public String metodoListar(Model model) {
        var productos = productoService.listar();
        productos.forEach(this::asignarIntegranteSeleccionado);
        model.addAttribute("productos", productos);
        return "/carpetaProductos/paginaProductos";
    }

    @GetMapping("nuevo")
    public String metodoNuevo(Model model) {
        var categorias = categoriaService.listar();
        System.out.println("DEBUG - Cantidad de categorías cargadas: " + categorias.size());
        
        model.addAttribute("producto", new ProductoDTO());
        model.addAttribute("listaCategorias", categorias);
        model.addAttribute("uuid", null); 
        
        return "/carpetaProductos/paginaFormulario";
    }

    @PostMapping("guardar")
    public String metodoGuardar(@Valid @ModelAttribute("producto") ProductoDTO producto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("listaCategorias", categoriaService.listar());
            return "/carpetaProductos/paginaFormulario";
        }
        productoService.guardar(producto);
        return "redirect:/rutaProductos/listar";
    }

    @GetMapping("editar/{uuid}")
    public String metodoActualizarForm(@PathVariable("uuid") UUID uuid, Model model) {
Producto producto = productoService.getUuid(uuid);
        
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setUuid(producto.getUuid());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setIdCategoria(producto.getCategoria().getIdCategoria());
        dto.setBrendaRV(producto.getBrendaRV());
        dto.setJoseArmandoBM(producto.getJoseArmandoBM());
        dto.setLizbethCL(producto.getLizbethCL());
        dto.setMairaPE(producto.getMairaPE());

        model.addAttribute("producto", dto);
        model.addAttribute("listaCategorias", categoriaService.listar());
        
        // PASAMOS EL UUID REAL: Para que el formulario sepa que es una edición y pinte "Editar Producto"
        model.addAttribute("uuid", uuid); 
        
        return "/carpetaProductos/paginaFormulario";
    }

    @PostMapping("actualizar")
    public String metodoActualizar(@Valid @ModelAttribute("producto") ProductoDTO producto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("listaCategorias", categoriaService.listar());
            model.addAttribute("uuid", producto.getUuid());
            return "/carpetaProductos/paginaFormulario";
        }
        productoService.actualizar(producto);
        return "redirect:/rutaProductos/listar";
    }
    
    @GetMapping("eliminar/{uuid}")
    public String metodoEliminar(@PathVariable("uuid") UUID uuid) {
        productoService.borrar(uuid); 
        return "redirect:/rutaProductos/listar";
    }
 // ... (Tus otros métodos de guardar, editar, eliminar, etc.)

    private void asignarIntegranteSeleccionado(Producto producto) {
        // Genera un número aleatorio entre 0 y 3
        int numeroAleatorio = (int) (Math.random() * 4);
        String valorSeleccionado;

        switch (numeroAleatorio) {
            case 0 -> valorSeleccionado = "Brenda: " + producto.getBrendaRV();
            case 1 -> valorSeleccionado = "Armando: " + producto.getJoseArmandoBM();
            case 2 -> valorSeleccionado = "Lizbeth: " + producto.getLizbethCL();
            case 3 -> valorSeleccionado = "Maira: " + producto.getMairaPE();
            default -> valorSeleccionado = "No asignado";
        }

        // Si el campo de ese integrante está vacío en la BD, le ponemos un texto por defecto
        if (valorSeleccionado == null || valorSeleccionado.isBlank()) {
            valorSeleccionado = "Sin comentarios";
        }

        // Asignamos el valor al campo temporal para que Thymeleaf pueda leerlo
        producto.setIntegranteSeleccionado(valorSeleccionado);
    }
}