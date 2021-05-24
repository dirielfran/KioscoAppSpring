package com.eareiza.springAngular.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Entity
@Table(name="gastos")
public class Gastos extends EntityCommon {

	private double montoPesos;
	private String descripcion;
	private Date fechaFact;
	private Date fechaCarga;
	private String usuario;
	private String proveedor;
	private String imagen= "+58kiosco.png";
	private String clasificacion;
	private double montoDolar;
	private double tasaDolar;
	
	private String metodopago;
	
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@OneToOne
	@JoinColumn(name="inventario_id")
	private Inventario inventario;

	@PrePersist
	@PreUpdate
	private void defaultValores() {
		this.fechaCarga = new Date();
	}
	public Gastos() {
		super();
	}
}
