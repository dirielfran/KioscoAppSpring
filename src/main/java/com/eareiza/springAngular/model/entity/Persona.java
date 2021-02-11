package com.eareiza.springAngular.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "personas")
public class Persona implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "per_nombre", nullable = false, unique = true)
	private String perNombre;
	
	@Column(name = "per_apellido",nullable = false, unique = true)	
	private String perApellido;
	
	@Column(name = "per_fecha_nacimiento",nullable = false, unique = true)
	private Date perFechaNacimiento;
	
	@Column(name = "per_numero_documento", nullable = false, unique = true)
	private Long perNumeroDocumento;
	
	@Column(name = "per_tipo_documento", nullable = false, unique = true)
	private String perTipoDocumento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPerNombre() {
		return perNombre;
	}

	public void setPerNombre(String perNombre) {
		this.perNombre = perNombre;
	}
	
	public String getPerApellido() {
		return perApellido;
	}
	
	public void setPerApellido(String perApellido) {
		this.perApellido = perApellido;
	}
	
	public Date getPerFechaNacimiento() {
		return perFechaNacimiento;
	}

	public void setPerFechaNacimiento(Date perFechaNacimiento) {
		this.perFechaNacimiento = perFechaNacimiento;
	}

	public Long getPerNumeroDocumento() {
		return perNumeroDocumento;
	}

	public void setPerNumeroDocumento(Long perNumeroDocumento) {
		this.perNumeroDocumento = perNumeroDocumento;
	}

	public String getPerTipoDocumento() {
		return perTipoDocumento;
	}

	public void setPerTipoDocumento(String perTipoDocumento) {
		this.perTipoDocumento = perTipoDocumento;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1677747681286131047L;

}
