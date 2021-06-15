package com.eareiza.springAngular.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ConsignacionDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6564813328390767815L;
	private String producto;
	private Double cantidad;
	private Double precio;
	private Long inventario;
	private Long productoId;

}
