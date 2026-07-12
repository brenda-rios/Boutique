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
import com.boutique.dto.DetallePedidoDTO;
import com.boutique.service.PedidoService;
import com.boutique.service.DetalleProductoService;
import com.boutique.service.CategoriaService;
import com.boutique.service.TallaService;
import com.boutique.repo.PedidoRepo;
import com.boutique.repo.DetallePedidoRepo;
import com.boutique.repo.DetalleProductoRepo;
import com.boutique.model.Pedido;
import java.util.List;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/rutaPedidos")
public class PedidoController {

    @Autowired
    PedidoService pedidoService;
    
    @Autowired
    DetalleProductoService detalleProductoService;
    
    @Autowired
    CategoriaService categoriaService;
    
    @Autowired
    TallaService tallaService;
    
    @Autowired
    PedidoRepo pedidoRepo;
    
    @Autowired
    DetallePedidoRepo detallePedidoRepo;
    
    @Autowired
    DetalleProductoRepo detalleProductoRepo;

    @GetMapping("listar")
    public String metodoListar(Model model) {
        model.addAttribute("pedidos", pedidoService.listar());
        return "/carpetaPedidos/paginaPedidos";
    }

    @GetMapping("nuevo")
    public String metodoNuevo(Model model) {
        PedidoDTO dto = new PedidoDTO();
        // Removemos la adición automática de una fila vacía para que la tabla inicie limpia
        // dto.getDetalles().add(new DetallePedidoDTO()); 
        
        model.addAttribute("pedido", dto);
        model.addAttribute("listaCategorias", categoriaService.listar());
        model.addAttribute("listaTallas", tallaService.listar());
        model.addAttribute("uuid", null);
        return "/carpetaPedidos/paginaFormulario";
    }

    @PostMapping("guardar")
    public String metodoGuardar(@Valid @ModelAttribute("pedido") PedidoDTO pedido, BindingResult result, Model model) {
        if (result.hasErrors()) {
            repopularDescripciones(pedido);
            model.addAttribute("listaCategorias", categoriaService.listar());
            model.addAttribute("listaTallas", tallaService.listar());
            model.addAttribute("uuid", null);
            return "/carpetaPedidos/paginaFormulario";
        }
        pedidoService.guardar(pedido);
        return "redirect:/rutaPedidos/listar";
    }

    @PostMapping("actualizar")
    public String metodoActualizar(@Valid @ModelAttribute("pedido") PedidoDTO pedido, BindingResult result, Model model) {
        if (result.hasErrors()) {
            repopularDescripciones(pedido);
            model.addAttribute("listaCategorias", categoriaService.listar());
            model.addAttribute("listaTallas", tallaService.listar());
            model.addAttribute("uuid", pedido.getUuid());
            return "/carpetaPedidos/paginaFormulario";
        }
        pedidoService.actualizar(pedido);
        return "redirect:/rutaPedidos/listar";
    }

    private void repopularDescripciones(PedidoDTO pedido) {
        if (pedido.getDetalles() != null) {
            for (DetallePedidoDTO det : pedido.getDetalles()) {
                if (det.getIdDetProd() != null) {
                    detalleProductoRepo.findById(det.getIdDetProd()).ifPresent(prod -> {
                        det.setDescripcionProducto(prod.getProducto().getNombre() + 
                            " - Talla: " + prod.getTalla().getNombre() + 
                            " - Color: " + prod.getColor().getNombre());
                    });
                }
            }
        }
    }

    @GetMapping("editar/{uuid}")
    public String metodoEditar(Model model, @PathVariable UUID uuid) {
        PedidoDTO dto = pedidoService.obtenerPedidoUUID(uuid);
        
        Pedido pedido = pedidoRepo.findByUuid(uuid).get();
        List<DetallePedidoDTO> detallesDto = detallePedidoRepo.findByPedido(pedido).stream()
                .map(det -> {
                    DetallePedidoDTO d = new DetallePedidoDTO();
                    d.setIdDetPed(det.getIdDetPed());
                    d.setIdPedido(pedido.getIdPedido());
                    d.setIdDetProd(det.getDetalleProducto().getIdDetProd());
                    d.setCantidad(det.getCantidad());
                    d.setPrecioUnitario(det.getPrecioUnitario());
                    d.setDescripcionProducto(det.getDetalleProducto().getProducto().getNombre() + 
                        " - Talla: " + det.getDetalleProducto().getTalla().getNombre() + 
                        " - Color: " + det.getDetalleProducto().getColor().getNombre());
                    return d;
                }).toList();
                
        dto.setDetalles(new java.util.ArrayList<>(detallesDto));
        
        model.addAttribute("pedido", dto);
        model.addAttribute("listaCategorias", categoriaService.listar());
        model.addAttribute("listaTallas", tallaService.listar());
        model.addAttribute("uuid", uuid);
        return "/carpetaPedidos/paginaFormulario";
    }

    @GetMapping("eliminar/{uuid}")
    public String metodoEliminar(Model model, @PathVariable UUID uuid) {
        pedidoService.borrar(uuid);
        return "redirect:/rutaPedidos/listar";
    }

    @GetMapping("ver/{uuid}")
    public String metodoVer(@PathVariable("uuid") UUID uuid, Model model) {
        PedidoDTO dto = pedidoService.obtenerPedidoUUID(uuid);
        Pedido pedido = pedidoRepo.findByUuid(uuid).get();
        
        List<DetallePedidoDTO> detallesDto = detallePedidoRepo.findByPedido(pedido).stream()
                .map(det -> {
                    DetallePedidoDTO d = new DetallePedidoDTO();
                    d.setIdDetPed(det.getIdDetPed());
                    d.setCantidad(det.getCantidad());
                    d.setPrecioUnitario(det.getPrecioUnitario());
                    d.setDescripcionProducto(det.getDetalleProducto().getProducto().getNombre() + 
                        " - Talla: " + det.getDetalleProducto().getTalla().getNombre() + 
                        " - Color: " + det.getDetalleProducto().getColor().getNombre());
                    return d;
                }).toList();
                
        dto.setDetalles(detallesDto);
        dto.setTotal(pedido.getTotal()); // Ensure total is mapped
        dto.setFechaHora(pedido.getFechaHora()); // Ensure fechaHora is mapped if it wasn't
        
        model.addAttribute("pedido", dto);
        return "/carpetaPedidos/verPedido";
    }
}