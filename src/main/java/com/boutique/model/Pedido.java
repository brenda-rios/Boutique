package com.boutique.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.boutique.enums.Estado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class Pedido {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long idPedido;

	    @Column(unique = true)
	    private UUID uuid;

	    private LocalDateTime fechaHora;

	    private Float total;
	    
        @Enumerated(EnumType.STRING)
	    private Estado estado;

	    @PrePersist
	    private void inicializarUuid() {
	        this.uuid = UUID.randomUUID();
	        if (this.fechaHora == null) {
	            this.fechaHora = LocalDateTime.now();
	        }
	    }

		public Long getIdPedido() {
			return idPedido;
		}

		public void setIdPedido(Long idPedido) {
			this.idPedido = idPedido;
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

		


	    
}
