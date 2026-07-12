package com.boutique.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.boutique.dto.DetalleProductoDTO;
import com.boutique.dto.ProductoDTO;
import com.boutique.model.Producto;
import com.boutique.model.DetalleProducto;
import com.boutique.model.Categoria;
import com.boutique.model.Color;
import com.boutique.model.Talla;
import com.boutique.repo.ProductoRepo;
import com.boutique.repo.CategoriaRepo;
import com.boutique.repo.DetalleProductoRepo;
import com.boutique.repo.ColorRepo;
import com.boutique.repo.TallaRepo;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private CategoriaRepo categoriaRepo;
    
    @Autowired
    private DetalleProductoRepo detalleProductoRepo;

    @Autowired
    private ColorRepo colorRepo;

    @Autowired
    private TallaRepo tallaRepo;

    @Override
    public List<Producto> listar() {
        return productoRepo.findAll();
    }

    @Override
    @Transactional
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

        producto = productoRepo.save(producto);
        
        // Guardar detalles
        if (dto.getDetalles() != null) {
            for (DetalleProductoDTO detDto : dto.getDetalles()) {
                DetalleProducto det = new DetalleProducto();
                det.setProducto(producto);
                det.setCantidad(detDto.getCantidad());
                det.setPrecio(detDto.getPrecio());
                
                Color color = colorRepo.findById(detDto.getIdColor())
                        .orElseThrow(() -> new IllegalArgumentException("Color no válido"));
                det.setColor(color);
                
                Talla talla = tallaRepo.findById(detDto.getIdTalla())
                        .orElseThrow(() -> new IllegalArgumentException("Talla no válida"));
                det.setTalla(talla);
                
                detalleProductoRepo.save(det);
            }
        }
    }

    @Override
    public Producto getUuid(UUID uuid) {
        return productoRepo.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con el UUID: " + uuid));
    }

    @Override
    @Transactional
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

        producto = productoRepo.save(producto);
        
        // Actualizar detalles (por simplicidad: borrar existentes y recrear, o actualizar si envían ID)
        // Eliminamos los detalles anteriores para reemplazarlos con los nuevos del formulario.
        List<DetalleProducto> actuales = detalleProductoRepo.findByProducto(producto);
        if (actuales != null) {
            detalleProductoRepo.deleteAll(actuales);
        }
        
        if (dto.getDetalles() != null) {
            for (DetalleProductoDTO detDto : dto.getDetalles()) {
                DetalleProducto det = new DetalleProducto();
                det.setProducto(producto);
                det.setCantidad(detDto.getCantidad());
                det.setPrecio(detDto.getPrecio());
                
                Color color = colorRepo.findById(detDto.getIdColor())
                        .orElseThrow(() -> new IllegalArgumentException("Color no válido"));
                det.setColor(color);
                
                Talla talla = tallaRepo.findById(detDto.getIdTalla())
                        .orElseThrow(() -> new IllegalArgumentException("Talla no válida"));
                det.setTalla(talla);
                
                detalleProductoRepo.save(det);
            }
        }
    }
    
    @Transactional
    public void borrar(UUID uuid) {
        Optional<Producto> optProducto = productoRepo.findByUuid(uuid);
        if (optProducto.isPresent()) {
            Producto producto = optProducto.get();
            // Eliminación en cascada de los detalles del producto
            detalleProductoRepo.deleteByProducto(producto);
            // Eliminación del producto padre
            productoRepo.delete(producto);
        } else {
            throw new jakarta.persistence.EntityNotFoundException("Producto no encontrado con UUID: " + uuid);
        }
    }
}