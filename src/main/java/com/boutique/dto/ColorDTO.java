package com.boutique.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public class ColorDTO {
	@NotBlank(message = "El nombre del color es obligatorio")
    private String nombre;
    private UUID uuid;
}
