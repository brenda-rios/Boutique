package com.boutique.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity
public class DetallePedido {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetPed;

    /*@ManyToOne
    @JoinColumn(name = "idPedido")
    private Pedido pedido;*/

    @ManyToOne
    @JoinColumn(name = "idDetProd")
    private DetalleProducto detalleProducto;

    private Integer cantidad;
    private Float precioUnitario;
    private Float subtotal;

    private UUID uuid;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
    }

}
