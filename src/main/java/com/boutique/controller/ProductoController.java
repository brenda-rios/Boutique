package com.boutique.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boutique.dto.ProductoDTO;
import com.boutique.dto.DetalleProductoDTO;
import com.boutique.model.Producto;
import com.boutique.service.CategoriaService;
import com.boutique.service.ProductoService;
import com.boutique.service.ColorService;
import com.boutique.service.TallaService;
import com.boutique.repo.DetalleProductoRepo;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/rutaProductos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private TallaService tallaService;
    
    @Autowired
    private DetalleProductoRepo detalleProductoRepo;

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
        
        ProductoDTO dto = new ProductoDTO();
        // Agregamos un detalle vacío por defecto para que Thymeleaf pueda renderizar la primera fila
        dto.getDetalles().add(new DetalleProductoDTO());
        
        model.addAttribute("producto", dto);
        model.addAttribute("listaCategorias", categorias);
        model.addAttribute("listaColores", colorService.listar());
        model.addAttribute("listaTallas", tallaService.listar());
        model.addAttribute("uuid", null); 
        
        return "/carpetaProductos/paginaFormulario";
    }

    @PostMapping("guardar")
    public String metodoGuardar(@Valid @ModelAttribute("producto") ProductoDTO producto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            
            // --- CÓDIGO DE DEPURACIÓN AÑADIDO ---
            System.out.println("========== ERRORES DE VALIDACIÓN DETECTADOS ==========");
            result.getFieldErrors().forEach(error -> {
                System.out.println("Campo fallido: " + error.getField());
                System.out.println("Motivo: " + error.getDefaultMessage());
                System.out.println("Valor que intentó guardar: " + error.getRejectedValue());
                System.out.println("------------------------------------------------------");
            });
            // ------------------------------------

            model.addAttribute("listaCategorias", categoriaService.listar());
            model.addAttribute("listaColores", colorService.listar());
            model.addAttribute("listaTallas", tallaService.listar());
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

        List<DetalleProductoDTO> detallesDto = detalleProductoRepo.findByProducto(producto).stream()
                .map(det -> {
                    DetalleProductoDTO d = new DetalleProductoDTO();
                    d.setIdDetProd(det.getIdDetProd());
                    d.setIdProducto(producto.getIdProducto());
                    d.setIdTalla(det.getTalla().getIdTalla());
                    d.setIdColor(det.getColor().getIdColor());
                    d.setCantidad(det.getCantidad());
                    d.setPrecio(det.getPrecio());
                    return d;
                }).toList();
        
        dto.setDetalles(new java.util.ArrayList<>(detallesDto)); // Mutable list

        model.addAttribute("producto", dto);
        model.addAttribute("listaCategorias", categoriaService.listar());
        model.addAttribute("listaColores", colorService.listar());
        model.addAttribute("listaTallas", tallaService.listar());
        
        // PASAMOS EL UUID REAL: Para que el formulario sepa que es una edición y pinte "Editar Producto"
        model.addAttribute("uuid", uuid); 
        
        return "/carpetaProductos/paginaFormulario";
    }

    @PostMapping("actualizar")
    public String metodoActualizar(@Valid @ModelAttribute("producto") ProductoDTO producto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("listaCategorias", categoriaService.listar());
            model.addAttribute("listaColores", colorService.listar());
            model.addAttribute("listaTallas", tallaService.listar());
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

    @GetMapping("/api/productos/filtrar")
    @ResponseBody
    public List<java.util.Map<String, Object>> filtrarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Long idTalla) {
        
        return detalleProductoRepo.findAll().stream()
                .filter(det -> {
                    boolean match = true;
                    if (nombre != null && !nombre.isBlank()) {
                        match = match && det.getProducto().getNombre().toLowerCase().contains(nombre.toLowerCase());
                    }
                    if (idCategoria != null) {
                        match = match && det.getProducto().getCategoria().getIdCategoria().equals(idCategoria);
                    }
                    if (idTalla != null) {
                        match = match && det.getTalla().getIdTalla().equals(idTalla);
                    }
                    return match;
                })
                .map(det -> {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("idDetProd", det.getIdDetProd());
                    map.put("texto", det.getProducto().getNombre() + " - Talla: " + det.getTalla().getNombre() + " - Color: " + det.getColor().getNombre() + " ($" + det.getPrecio() + ")");
                    return map;
                })
                .toList();
    }
    @GetMapping("ver/{uuid}")
    public String metodoVer(@PathVariable("uuid") UUID uuid, Model model) {
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

        List<DetalleProductoDTO> detallesDto = detalleProductoRepo.findByProducto(producto).stream()
                .map(det -> {
                    DetalleProductoDTO d = new DetalleProductoDTO();
                    d.setIdDetProd(det.getIdDetProd());
                    d.setIdProducto(producto.getIdProducto());
                    d.setTallaNombre(det.getTalla().getNombre());
                    d.setColorNombre(det.getColor().getNombre());
                    d.setCantidad(det.getCantidad());
                    d.setPrecio(det.getPrecio());
                    return d;
                }).toList();
        
        dto.setDetalles(detallesDto);
        model.addAttribute("producto", dto);
        model.addAttribute("categoriaNombre", producto.getCategoria().getNombre());
        
        return "/carpetaProductos/verProducto";
    }

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