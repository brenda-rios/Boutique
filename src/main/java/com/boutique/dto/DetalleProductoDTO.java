package com.boutique.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class DetalleProductoDTO {
	@NotNull(message = "El producto es obligatorio")
    private Integer idProducto;
    @NotNull(message = "La talla es obligatoria")
    private Integer idTalla;
    @NotNull(message = "El color es obligatorio")
    private Integer idColor;
    @NotNull(message = "Este campo es obligatorio")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;
    @NotNull(message = "Este campo es obligatorio")
    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private Float precio;
    private UUID uuid;
	public Integer getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}
	public Integer getIdTalla() {
		return idTalla;
	}
	public void setIdTalla(Integer idTalla) {
		this.idTalla = idTalla;
	}
	public Integer getIdColor() {
		return idColor;
	}
	public void setIdColor(Integer idColor) {
		this.idColor = idColor;
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
