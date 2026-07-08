package com.boutique.dto;

public class DetallePedidoDTO {
	private Long idDetPed;
    private Long idPedido;
    private Long idDetProd;
    private Integer cantidad;
    private Float precioUnitario;
    
    
	public Long getIdDetPed() {
		return idDetPed;
	}
	public void setIdDetPed(Long idDetPed) {
		this.idDetPed = idDetPed;
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
