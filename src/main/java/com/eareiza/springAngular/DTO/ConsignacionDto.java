package com.eareiza.springAngular.DTO;

import java.io.Serializable;

public class ConsignacionDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6564813328390767815L;
	private String producto;
	private Double cantidad;
	private Double precio;
	private Long inventario;
	private Long factura;

	public ConsignacionDto() {
		super();
	}
	
	public ConsignacionDto(String producto, Double cantidad, Double precio, Long inventario, Long factura) {
		super();
		this.producto = producto;
		this.cantidad = cantidad;
		this.precio = precio;
		this.inventario = inventario;
		this.factura = factura;
	}
	
	public Long getFactura() {
		return factura;
	}

	public void setFactura(Long factura) {
		this.factura = factura;
	}

	public Long getInventario() {
		return inventario;
	}

	public void setInventario(Long inventario) {
		this.inventario = inventario;
	}

	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public Double getCantidad() {
		return cantidad;
	}
	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	@Override
	public String toString() {
		return "ConsignacionDto [producto=" + producto + ", cantidad=" + cantidad + ", precio=" + precio + "]";
	}	
	
}
