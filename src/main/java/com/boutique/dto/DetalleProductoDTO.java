package com.boutique.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DetalleProductoDTO {
    private Long idDetProd;


    private Long idProducto;

    @NotNull(message = "La talla es obligatoria")
    private Long idTalla;

    @NotNull(message = "El color es obligatorio")
    private Long idColor;

    private String productoNombre;
    private String tallaNombre;
    private String colorNombre;

    @NotNull(message = "Este campo es obligatorio")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    @NotNull(message = "Este campo es obligatorio")
    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private Float precio;

    private UUID uuid;

    public Long getIdDetProd() {
        return idDetProd;
    }

    public void setIdDetProd(Long idDetProd) {
        this.idDetProd = idDetProd;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getIdTalla() {
        return idTalla;
    }

    public void setIdTalla(Long idTalla) {
        this.idTalla = idTalla;
    }

    public Long getIdColor() {
        return idColor;
    }

    public void setIdColor(Long idColor) {
        this.idColor = idColor;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public String getTallaNombre() {
        return tallaNombre;
    }

    public void setTallaNombre(String tallaNombre) {
        this.tallaNombre = tallaNombre;
    }

    public String getColorNombre() {
        return colorNombre;
    }

    public void setColorNombre(String colorNombre) {
        this.colorNombre = colorNombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
