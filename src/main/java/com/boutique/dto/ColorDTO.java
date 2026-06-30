package com.boutique.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public class ColorDTO {
	@NotBlank(message = "El nombre del color es obligatorio")
    private String nombre;
    private UUID uuid;
	public UUID getUuid() {
		// TODO Auto-generated method stub
		return uuid;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	
}
