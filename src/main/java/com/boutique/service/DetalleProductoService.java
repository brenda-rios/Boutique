package com.boutique.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutique.dto.DetalleProductoDTO;
import com.boutique.model.Color;
import com.boutique.model.DetalleProducto;
import com.boutique.model.Producto;
import com.boutique.model.Talla;
import com.boutique.repo.ColorRepo;
import com.boutique.repo.DetalleProductoRepo;
import com.boutique.repo.ProductoRepo;
import com.boutique.repo.TallaRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DetalleProductoService {
    @Autowired
    private DetalleProductoRepo detalleProductoRepo;

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private TallaRepo tallaRepo;

    @Autowired
    private ColorRepo colorRepo;

    public List<DetalleProductoDTO> listar() {
        return detalleProductoRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public void guardar(DetalleProductoDTO detalleProducto) {
        DetalleProducto detalle = new DetalleProducto();
        detalle.setProducto(findProducto(detalleProducto.getIdProducto()));
        detalle.setTalla(findTalla(detalleProducto.getIdTalla()));
        detalle.setColor(findColor(detalleProducto.getIdColor()));
        detalle.setCantidad(detalleProducto.getCantidad());
        detalle.setPrecio(detalleProducto.getPrecio());
        detalleProductoRepo.save(detalle);
    }

    public void actualizar(DetalleProductoDTO detalleProducto) {
        Optional<DetalleProducto> optDetalle = detalleProductoRepo.findByUuid(detalleProducto.getUuid());
        if (optDetalle.isPresent()) {
            DetalleProducto detalle = optDetalle.get();
            detalle.setProducto(findProducto(detalleProducto.getIdProducto()));
            detalle.setTalla(findTalla(detalleProducto.getIdTalla()));
            detalle.setColor(findColor(detalleProducto.getIdColor()));
            detalle.setCantidad(detalleProducto.getCantidad());
            detalle.setPrecio(detalleProducto.getPrecio());
            detalleProductoRepo.save(detalle);
        } else {
            throw new EntityNotFoundException("Detalle no encontrado con UUID: " + detalleProducto.getUuid());
        }
    }

    public void borrar(UUID uuid) {
        Optional<DetalleProducto> optDetalle = detalleProductoRepo.findByUuid(uuid);
        if (optDetalle.isPresent()) {
            detalleProductoRepo.delete(optDetalle.get());
        } else {
            throw new EntityNotFoundException("Detalle no encontrado con UUID: " + uuid);
        }
    }

    public DetalleProductoDTO obtenerDetalleProductoUUID(UUID uuid) {
        Optional<DetalleProducto> optDetalle = detalleProductoRepo.findByUuid(uuid);
        if (optDetalle.isPresent()) {
            return mapToDto(optDetalle.get());
        } else {
            throw new EntityNotFoundException("Detalle no encontrado con UUID: " + uuid);
        }
    }

    private DetalleProductoDTO mapToDto(DetalleProducto detalle) {
        DetalleProductoDTO dto = new DetalleProductoDTO();
        dto.setIdDetProd(detalle.getIdDetProd());
        dto.setIdProducto(detalle.getProducto() != null ? detalle.getProducto().getIdProducto() : null);
        dto.setProductoNombre(detalle.getProducto() != null ? detalle.getProducto().getNombre() : null);
        dto.setIdTalla(detalle.getTalla() != null ? detalle.getTalla().getIdTalla() : null);
        dto.setTallaNombre(detalle.getTalla() != null ? detalle.getTalla().getNombre() : null);
        dto.setIdColor(detalle.getColor() != null ? detalle.getColor().getIdColor() : null);
        dto.setColorNombre(detalle.getColor() != null ? detalle.getColor().getNombre() : null);
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecio(detalle.getPrecio());
        dto.setUuid(detalle.getUuid());
        return dto;
    }

    private Producto findProducto(Long idProducto) {
        return productoRepo.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no válido"));
    }

    private Talla findTalla(Long idTalla) {
        return tallaRepo.findById(idTalla)
                .orElseThrow(() -> new IllegalArgumentException("Talla no válida"));
    }

    private Color findColor(Long idColor) {
        return colorRepo.findById(idColor)
                .orElseThrow(() -> new IllegalArgumentException("Color no válido"));
    }
}
