package com.boutique.dto;

import java.util.UUID;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DetallePedidoDTO {
    private Long idDetPed;
    private UUID uuid;
    private Long idPedido;
    
    @NotNull(message = "El producto es obligatorio")
    private Long idDetProd;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    // Puede venir nulo desde el form si calculamos en server, pero si el form lo envía, lo permitimos.
    private Float precioUnitario;
    
    private String descripcionProducto;

    public String getDescripcionProducto() {
        return descripcionProducto;
    }
    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public Long getIdDetPed() {
        return idDetPed;
    }
    public void setIdDetPed(Long idDetPed) {
        this.idDetPed = idDetPed;
    }
    public UUID getUuid() {
        return uuid;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    public Long getIdPedido() {
        return idPedido;
    }
    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }
    public Long getIdDetProd() {
        return idDetProd;
    }
    public void setIdDetProd(Long idDetProd) {
        this.idDetProd = idDetProd;
    }
    public Integer getCantidad() {
        return cantidad;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    public Float getPrecioUnitario() {
        return precioUnitario;
    }
    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
