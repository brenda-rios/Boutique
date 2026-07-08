package com.boutique.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.boutique.enums.Estado;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PedidoDTO {

	    private Long idPedido;

	    private UUID uuid;

	    private LocalDateTime fechaHora; 

	    @NotNull(message = "El total es obligatorio")
	    @Min(value = 0, message = "El total no puede ser negativo")
	    private Float total;

	    @NotNull(message = "El estado es obligatorio")
	    private Estado estado;

	    // Getters y Setters
	    public Long getIdPedido() { return idPedido; }
	    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
	    public UUID getUuid() { return uuid; }
	    public void setUuid(UUID uuid) { this.uuid = uuid; }
	    public LocalDateTime getFechaHora() { return fechaHora; }
	    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
	    public Float getTotal() { return total; }
	    public void setTotal(Float total) { this.total = total; }
	    public Estado getEstado() { return estado; }
	    public void setEstado(Estado estado) { this.estado = estado; }
	}

