package com.boutique.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.boutique.enums.Estado;

public class VentaResumenDTO {
    private UUID uuid;
    private LocalDateTime fechaHora;
    private Float total;
    private Estado estado;
    private String integrante;
    private int nLineas;

    public VentaResumenDTO() {
    }

    public VentaResumenDTO(UUID uuid, LocalDateTime fechaHora, Float total, Estado estado, String integrante, int nLineas) {
        this.uuid = uuid;
        this.fechaHora = fechaHora;
        this.total = total;
        this.estado = estado;
        this.integrante = integrante;
        this.nLineas = nLineas;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getIntegrante() {
        return integrante;
    }

    public void setIntegrante(String integrante) {
        this.integrante = integrante;
    }

    public int getnLineas() {
        return nLineas;
    }

    public void setnLineas(int nLineas) {
        this.nLineas = nLineas;
    }
}
