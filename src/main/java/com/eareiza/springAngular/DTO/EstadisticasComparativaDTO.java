package com.eareiza.springAngular.DTO;

import java.io.Serializable;

public class EstadisticasComparativaDTO implements Serializable{
	
	private String nombre;
	private Integer mes;
	private Double ventas;
	private Double ganancias;
	private Double gastos;	
	
	public EstadisticasComparativaDTO(Integer mes, Double ventas, Double ganancias, Double gastos) {
		super();
		this.mes = mes;
		this.ventas = ventas;
		this.ganancias = ganancias;
		this.gastos = gastos;
	}
	
	
	public EstadisticasComparativaDTO(String nombre, Double ganancias) {
		super();
		this.nombre = nombre;
		this.ganancias = ganancias;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Double getVentas() {
		return ventas;
	}
	public void setVentas(Double ventas) {
		this.ventas = ventas;
	}
	public Double getGanancias() {
		return ganancias;
	}
	public void setGanancias(Double ganancias) {
		this.ganancias = ganancias;
	}
	public Double getGastos() {
		return gastos;
	}
	public void setGastos(Double gastos) {
		this.gastos = gastos;
	}
	
	
	
	@Override
	public String toString() {
		return "EstadisticasComparativaDTO [mes=" + mes + ", ventas=" + ventas + ", ganancias=" + ganancias
				+ ", gastos=" + gastos + "]";
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 7095604405310509602L;	

}
