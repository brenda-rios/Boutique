package com.boutique.service;

import com.boutique.dto.ProductoDTO;
import com.boutique.model.Producto;
import java.util.List;
import java.util.UUID;

public interface ProductoService {
    List<Producto> listar();
    void guardar(ProductoDTO dto);
    Producto getUuid(UUID uuid);
    void actualizar(ProductoDTO dto);
    void borrar(UUID uuid);
    
    List<Producto> listarPorCategoria(Long idCategoria);
}
