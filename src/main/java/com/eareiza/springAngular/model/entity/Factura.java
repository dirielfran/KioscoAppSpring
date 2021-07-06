package com.eareiza.springAngular.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@Table(name="facturas")
@JsonIgnoreProperties (ignoreUnknown = true)
public class Factura extends EntityCommon implements Serializable{

	private String descripcion;
	private String observacion;	
	private Double mercadopago;
	private Double pedidosya;
	private Double puntoventa;
	private Double montocomision;
	private Double pedidosyaefectivo;
	@NotNull
	private boolean costo;
	
	@Transient
	private Map<String, String> tipopago;
	
	private Double total;
		
	@JsonIgnoreProperties(value = {"facturas", "hibernateLazyInitializer", "handler"}, allowSetters = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name = "caja_id")
	private Caja caja;

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "factura_id")
	private List<ItemFactura> items;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne
	@JoinColumn(name = "comision_id")
	private Comision comision;
	
	public Factura() {
		this.items = new ArrayList<>();
	}

	private static final long serialVersionUID = 1L;
}
