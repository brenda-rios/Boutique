package com.boutique.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutique.dto.DetalleProductoDTO;
import com.boutique.model.DetalleProducto;
import com.boutique.repo.DetalleProductoRepo;

@Service
public class DetalleProductoService {
    @Autowired
    DetalleProductoRepo detalleProductoRepo;
    @Autowired
    ModelMapper mapper;

    public List<DetalleProductoDTO> listar() {
        return detalleProductoRepo.findAll().stream()
                .map(detalle -> mapper.map(detalle, DetalleProductoDTO.class))
                .toList();
    }

    public void guardar(DetalleProductoDTO detalleProducto) {
        detalleProductoRepo.save(mapper.map(detalleProducto, DetalleProducto.class));
    }
}
