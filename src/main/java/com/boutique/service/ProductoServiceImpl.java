package com.boutique.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutique.dto.ProductoDTO;
import com.boutique.model.Producto;
import com.boutique.model.Categoria;
import com.boutique.repo.ProductoRepo;
import com.boutique.repo.CategoriaRepo;
import com.boutique.repo.DetalleProductoRepo;
import com.boutique.exception.ReferencedEntityException;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private CategoriaRepo categoriaRepo;
    
    @Autowired
    private DetalleProductoRepo detalleProductoRepo;

    @Override
    public List<Producto> listar() {
        return productoRepo.findAll();
    }

    @Override
    public void guardar(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setBrendaRV(dto.getBrendaRV());
        producto.setJoseArmandoBM(dto.getJoseArmandoBM());
        producto.setLizbethCL(dto.getLizbethCL());
        producto.setMairaPE(dto.getMairaPE());
        
        // Buscamos la categoría de forma segura
        Categoria cat = categoriaRepo.findById(dto.getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no válida"));
        producto.setCategoria(cat);

        productoRepo.save(producto);
    }

    @Override
    public Producto getUuid(UUID uuid) {
        return productoRepo.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con el UUID: " + uuid));
    }

    @Override
    public void actualizar(ProductoDTO dto) {
        Producto producto = productoRepo.findByUuid(dto.getUuid())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
                
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setBrendaRV(dto.getBrendaRV());
        producto.setJoseArmandoBM(dto.getJoseArmandoBM());
        producto.setLizbethCL(dto.getLizbethCL());
        producto.setMairaPE(dto.getMairaPE());

        Categoria cat = categoriaRepo.findById(dto.getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no válida"));
        producto.setCategoria(cat);

        productoRepo.save(producto);
    }
    
    public void borrar(UUID uuid) {
        Optional<Producto> optProducto = productoRepo.findByUuid(uuid);
        if (optProducto.isPresent()) {
            Producto producto = optProducto.get();
            // Validar que no hay detalles de producto asociados
            long detallesCount = detalleProductoRepo.countByProducto(producto);
            if (detallesCount > 0) {
                throw new ReferencedEntityException(
                    "No se puede eliminar este producto porque está siendo utilizado en " + detallesCount + " detalle(s) de producto."
                );
            }
            productoRepo.delete(producto);
        } else {
            throw new jakarta.persistence.EntityNotFoundException("Producto no encontrado con UUID: " + uuid);
        }
    }
}