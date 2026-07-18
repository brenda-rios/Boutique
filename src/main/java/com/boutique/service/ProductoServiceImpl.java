package com.boutique.service;

import java.util.List;
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
import com.boutique.repo.DetallePedidoRepo;
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
    
    @Autowired
    private DetallePedidoRepo detallePedidoRepo;

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
        
        // Guardar detalles con Validación de Duplicados
        if (dto.getDetalles() != null) {
            // Estructura para recordar las combinaciones y evitar repetidos
            java.util.Set<String> combinacionesVistas = new java.util.HashSet<>();
            
            for (DetalleProductoDTO detDto : dto.getDetalles()) {
                // Creamos una llave única, ej: "2-4" (IdTalla-IdColor)
                String combinacionUnica = detDto.getIdTalla() + "-" + detDto.getIdColor();
                
                // Si el HashSet no puede agregarlo, es porque ya existe esa combinación en la lista
                if (!combinacionesVistas.add(combinacionUnica)) {
                    throw new IllegalArgumentException("Error: No puedes registrar la misma combinación de Talla y Color más de una vez en este producto.");
                }

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
        
        // Eliminamos los detalles anteriores para reemplazarlos con los nuevos del formulario.
        List<DetalleProducto> actuales = detalleProductoRepo.findByProducto(producto);
        if (actuales != null) {
            detalleProductoRepo.deleteAll(actuales);
        }
        
        // Guardar los nuevos detalles con Validación de Duplicados
        if (dto.getDetalles() != null) {
            java.util.Set<String> combinacionesVistas = new java.util.HashSet<>();

            for (DetalleProductoDTO detDto : dto.getDetalles()) {
                // Validación estricta
                String combinacionUnica = detDto.getIdTalla() + "-" + detDto.getIdColor();
                if (!combinacionesVistas.add(combinacionUnica)) {
                    throw new IllegalArgumentException("Error al actualizar: Has ingresado variantes duplicadas con la misma Talla y Color.");
                }

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
    @Transactional
    public void borrar(UUID uuid) {
        Producto producto = productoRepo.findByUuid(uuid)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Producto no encontrado"));

        // --- VALIDACIÓN DE SEGURIDAD (AQUÍ ESTÁ LA LÓGICA) ---
        // Obtenemos todas las variantes (DetalleProducto) de este producto
        List<DetalleProducto> variantes = detalleProductoRepo.findByProducto(producto);
        
        // Revisamos si alguna de esas variantes tiene algún pedido asociado
        for (DetalleProducto variante : variantes) {
            if (detallePedidoRepo.existsByDetalleProducto(variante)) {
            	throw new IllegalStateException(
                        "No se puede eliminar este producto porque una o más de sus variantes ya fueron utilizadas en pedidos registrados.");
                            }
        }
        // -----------------------------------------------------

        // Si el código llega aquí es porque NO tiene pedidos, podemos borrar tranquilos
        detalleProductoRepo.deleteByProducto(producto);
        productoRepo.delete(producto);
    }
    
    @Override
    public List<Producto> listarPorCategoria(Long idCategoria) {
        // Este método usa el repositorio para buscar productos que pertenezcan 
        // al ID de la categoría que el usuario seleccionó en el filtro.
        return productoRepo.findByCategoriaIdCategoria(idCategoria);
    }
}