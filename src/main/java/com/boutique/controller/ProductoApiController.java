package com.boutique.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boutique.model.DetalleProducto;
import com.boutique.repo.DetalleProductoRepo;

@RestController
@RequestMapping("/api/productos")
public class ProductoApiController {

    @Autowired
    private DetalleProductoRepo detalleProductoRepo;

    @GetMapping("/filtrar")
    public List<Map<String, Object>> filtrarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String idCategoria,
            @RequestParam(required = false) String idTalla) {
        // Obtenemos todos los detalles (variantes) reales de la base de datos
        List<DetalleProducto> todosLosDetalles = detalleProductoRepo.findAll();
        
        // Filtramos la lista según los parámetros que envíe el navegador
     // Filtramos la lista según los parámetros que envíe el navegador
        return todosLosDetalles.stream()
                .filter(det -> {
                    // 1. Filtro Nombre: Si es null o vacío, pasa (true). Si no, valida.
                    boolean matchNombre = (nombre == null || nombre.isBlank()) || 
                            (det.getProducto() != null && det.getProducto().getNombre() != null && 
                             det.getProducto().getNombre().toLowerCase().contains(nombre.toLowerCase()));
                    
                    // 2. Filtro Categoría
                    // Verificamos que det.getProducto() y getCategoria() existan antes de acceder
                    boolean matchCategoria = (idCategoria == null || idCategoria.isBlank()) || 
                            (det.getProducto() != null && 
                             det.getProducto().getCategoria() != null && 
                             String.valueOf(det.getProducto().getCategoria().getIdCategoria()).equals(idCategoria));
                    
                    // 3. Filtro Talla
                    // Aquí está la clave: verificamos si det.getTalla() no es null
                    boolean matchTalla = (idTalla == null || idTalla.isBlank()) || 
                            (det.getTalla() != null && 
                             String.valueOf(det.getTalla().getIdTalla()).equals(idTalla));
                    
                    // Retorna verdadero solo si cumple los 3 criterios activos
                    return matchNombre && matchCategoria && matchTalla;
                })
                .map(det -> {
                    // Construimos el objeto JSON que espera el JavaScript
                    Map<String, Object> map = new HashMap<>();
                    map.put("idDetProd", det.getIdDetProd());
                    
                    // Este será el texto visible en el <select>
                    String textoVisible = det.getProducto().getNombre() + 
                            " - Talla: " + det.getTalla().getNombre() + 
                            " - Color: " + det.getColor().getNombre() + 
                            " ($" + det.getPrecio() + ")";
                            
                    map.put("texto", textoVisible);
                    map.put("cantidad", det.getCantidad());

                    return map;
                })
                .collect(Collectors.toList());
    }
}