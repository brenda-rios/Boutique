package com.boutique.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public class TallaDTO {
	private Long idTalla;
	@NotBlank(message = "El nombre de la talla es obligatorio")
    private String nombre;
    private UUID uuid;
    
    
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
	public Long getIdTalla() {
		return idTalla;
	}
	public void setIdTalla(Long idTalla) {
		this.idTalla = idTalla;
	}
    
    

}
