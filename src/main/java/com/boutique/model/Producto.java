package com.boutique.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.UUID;

@Entity
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "uuid", unique = true, nullable = false)
    private UUID uuid;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    // CORRECCIÓN: Cambiamos LAZY por EAGER para evitar el Error 500 al mapear en el formulario
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "BrendaRV")
    private String brendaRV;

    @Column(name = "JoseArmandoBM")
    private String joseArmandoBM;

    @Column(name = "LizbethCL")
    private String lizbethCL;

    @Column(name = "MairaPE")
    private String mairaPE;

    @Transient
    private String integranteSeleccionado;

    public Producto() {
        this.uuid = UUID.randomUUID();
    }

    // Getters y Setters
    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

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
}
