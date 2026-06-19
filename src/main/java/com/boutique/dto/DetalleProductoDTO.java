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

}
