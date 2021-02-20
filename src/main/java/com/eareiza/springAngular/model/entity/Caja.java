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

@Entity
@Table(name = "cajas")
public class Caja implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Temporal(TemporalType.DATE)
	private Date fechaopen;
	@Temporal(TemporalType.DATE)
	private Date fechaclose;
	@Temporal(TemporalType.DATE)
	private Date fechamod;
	private Double venta;
	private Double retiros;
	private Double mercadopago;
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

	public Double getRetiros() {
		return retiros;
	}

	public void setRetiros(Double retiros) {
		this.retiros = retiros;
	}

	public List<RetiroCaja> getRetiroscaja() {
		return retirosCaja;
	}
	
	//Se utiliza el metodo addretiro para añadir la lista de retiros y seteo de Caja
	public void setRetiroscaja(List<RetiroCaja> retiros) {
		this.retirosCaja.clear();
		retiros.forEach(this::addRetiro);
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFechaopen() {
		return fechaopen;
	}
	public void setFechaopen(Date fechaopen) {
		this.fechaopen = fechaopen;
	}
	public Date getFechaclose() {
		return fechaclose;
	}
	public void setFechaclose(Date fechaclose) {
		this.fechaclose = fechaclose;
	}
	public Date getFechamod() {
		return fechamod;
	}
	public void setFechamod(Date fechamod) {
		this.fechamod = fechamod;
	}
	public Double getVenta() {
		return venta;
	}
	public void setVenta(Double venta) {
		this.venta = venta;
	}
	public Double getMercadopago() {
		return mercadopago;
	}
	public void setMercadopago(Double mercadopago) {
		this.mercadopago = mercadopago;
	}
	public Double getIniciocaja() {
		return iniciocaja;
	}
	public void setIniciocaja(Double iniciocaja) {
		this.iniciocaja = iniciocaja;
	}
	public Double getDiferencia() {
		return diferencia;
	}
	public void setDiferencia(Double diferencia) {
		this.diferencia = diferencia;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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

	public Double getGanancia() {
		return ganancia;
	}

	public void setGanancia(Double ganancia) {
		this.ganancia = ganancia;
	}

	public List<RetiroCaja> getRetirosCaja() {
		return retirosCaja;
	}

	public void setRetirosCaja(List<RetiroCaja> retirosCaja) {
		this.retirosCaja = retirosCaja;
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = -5375172429629965039L;	

}
