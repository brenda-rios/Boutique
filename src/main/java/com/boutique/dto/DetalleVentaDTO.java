package com.boutique.dto;

public class DetalleVentaDTO {
    private String productoNombre;
    private String categoriaNombre;
    private String color;
    private String talla;
    private Integer cantidad;
    private Float precioUnitario;
    private Float subtotal;

    public DetalleVentaDTO() {
    }

    public DetalleVentaDTO(String productoNombre, String categoriaNombre, String color, String talla, Integer cantidad, Float precioUnitario, Float subtotal) {
        this.productoNombre = productoNombre;
        this.categoriaNombre = categoriaNombre;
        this.color = color;
        this.talla = talla;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }
}
