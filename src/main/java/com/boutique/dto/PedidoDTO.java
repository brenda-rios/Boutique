package com.boutique.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.boutique.enums.Estado;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class PedidoDTO {

    private Long idPedido;

    private UUID uuid;

    private LocalDateTime fechaHora;

    private Float total;

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    private String brendaRV;
    private String joseArmandoBM;
    private String lizbethCL;
    private String mairaPE;

    private String integranteSeleccionado;

    
    private List<@Valid DetallePedidoDTO> detalles = new ArrayList<>();

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

    public String getBrendaRV() {
        return brendaRV;
    }

    public void setBrendaRV(String brendaRV) {
        this.brendaRV = brendaRV;
    }

    public String getJoseArmandoBM() {
        return joseArmandoBM;
    }

    public void setJoseArmandoBM(String joseArmandoBM) {
        this.joseArmandoBM = joseArmandoBM;
    }

    public String getLizbethCL() {
        return lizbethCL;
    }

    public void setLizbethCL(String lizbethCL) {
        this.lizbethCL = lizbethCL;
    }

    public String getMairaPE() {
        return mairaPE;
    }

    public void setMairaPE(String mairaPE) {
        this.mairaPE = mairaPE;
    }

    public String getIntegranteSeleccionado() {
        return integranteSeleccionado;
    }

    public void setIntegranteSeleccionado(String integranteSeleccionado) {
        this.integranteSeleccionado = integranteSeleccionado;
    }

    public List<DetallePedidoDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedidoDTO> detalles) {
        this.detalles = detalles;
    }
}
