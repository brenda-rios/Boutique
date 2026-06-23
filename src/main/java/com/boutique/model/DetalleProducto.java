package com.boutique.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class DetalleProducto {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetProd;
    @Column
    private Integer idProducto;
    @Column
    private Integer idTalla;
    @Column
    private Integer idColor;
    @Column
    private Integer cantidad;
    @Column
    private Float precio;
    @Column(unique=true)
    private UUID uuid;

    @PrePersist
    private void inicializarUuid() {
        this.uuid = UUID.randomUUID();
    }

	public Long getIdDetProd() {
		return idDetProd;
	}

	public void setIdDetProd(Long idDetProd) {
		this.idDetProd = idDetProd;
	}

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
