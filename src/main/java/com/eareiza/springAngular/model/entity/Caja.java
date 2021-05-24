package com.eareiza.springAngular.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Entity
@Table(name = "cajas")
public class Caja extends EntityCommon implements Serializable{

	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaopen;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaclose;
	private Double venta;
	private Double retiros;
	private Double mercadopago;
	private Double pedidosya;
	private Double pedidosyaefectivo;
	private Double puntoventa;
	private Double iniciocaja = 2000.0;
	private Double diferencia;
	private Double ganancia;
	private String estado = "Abierto";
	private String observacion;
	
	//Se crea relacion con la clase Factura
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caja", cascade = CascadeType.ALL)
	@JsonIgnoreProperties(value = {"facturas", "hibernateLazyInitializer", "handler"}, allowSetters = true)
	private List<Factura> facturas;
	
	@JsonIgnoreProperties(value= {"caja"}, allowSetters = true)
	@OneToMany(mappedBy = "caja",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true )
	private List<RetiroCaja> retirosCaja;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@PrePersist
	private void preFechaOpen() {
		this.fechaopen = new Date();
	}
	
	public Caja() {
		this.facturas = new ArrayList<>();
		this.retirosCaja = new ArrayList<>();
	}	

	//Se utiliza el metodo addretiro para añadir la lista de retiros y seteo de Caja
	public void setRetiroscaja(List<RetiroCaja> retiros) {
		this.retirosCaja.clear();
		retiros.forEach(this::addRetiro);
	}

	public void addRetiro(RetiroCaja retiro ) {
		this.retirosCaja.add(retiro);
		//relacion inversa
		retiro.setCaja(this);
	}
	
	public void deleteRetiro(RetiroCaja retiro) {
		this.retirosCaja.remove(retiro);
		//Se estable relacion inversa al setearle null se elimana por la 
		//propiedad config. orphanRemoval = true
		retiro.setCaja(null);
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -5375172429629965039L;
}
