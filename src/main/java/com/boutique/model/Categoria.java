package com.boutique.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class Categoria {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long idCategoria;
	    @Column
	    private String nombre;
	    @Column
	    private String descripcion;
	    @Column(unique=true)
	    private UUID uuid;

	    @PrePersist
	    private void inicializarUuid() {
	        this.uuid = UUID.randomUUID();
	    }

		public Long getIdCategoria() {
			return idCategoria;
		}

		public void setIdCategoria(Long idCategoria) {
			this.idCategoria = idCategoria;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

		public UUID getUuid() {
			return uuid;
		}

		public void setUuid(UUID uuid) {
			this.uuid = uuid;
		}
	    
	    

}
