package com.boutique.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class Color {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idColor;
    @Column
    private String nombre;
    @Column(unique=true)
    private UUID uuid;

    
    @PrePersist
    private void inicializarUuid() {
        this.uuid = UUID.randomUUID();
    }


	public Long getIdColor() {
		return idColor;
	}


	public void setIdColor(Long idColor) {
		this.idColor = idColor;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public UUID getUuid() {
		return uuid;
	}


	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
    
    
    
    
}
