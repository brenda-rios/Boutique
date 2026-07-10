package com.boutique.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductoDTO {
    private Long idProducto;
    private UUID uuid;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "Debes seleccionar una categoría")
    private Long idCategoria;
    private String brendaRV;
    private String joseArmandoBM;
    private String lizbethCL;
    private String mairaPE;

    // CORRECCIÓN: Constructor vacío explícito obligatorio para Thymeleaf
    public ProductoDTO() {
    }

    // Getters y Setters limpios
    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getBrendaRV() {
        return brendaRV;
    }

    public void setBrendaRV(String brendaRV) {
        this.brendaRV = brendaRV;
    }

    public String getJoseArmandoBM() {
        return joseArmandoBM;
    }

    public void setJoseArmandoBM(String joseArmandoBM) {
        this.joseArmandoBM = joseArmandoBM;
    }

    public String getLizbethCL() {
        return lizbethCL;
    }

    public void setLizbethCL(String lizbethCL) {
        this.lizbethCL = lizbethCL;
    }

    public String getMairaPE() {
        return mairaPE;
    }

    public void setMairaPE(String mairaPE) {
        this.mairaPE = mairaPE;
    }
}
