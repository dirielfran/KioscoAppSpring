package com.eareiza.springAngular.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "cajachica")
public class Cajachica implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8134548246055327266L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	
	private Double saldomp;
	private Double saldoefectivo;
	private Double monto;
	
	
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@OneToOne
	@JoinColumn(name="factura_id")
	private Factura factura;
	
	
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@OneToOne
	@JoinColumn(name="gasto_id")
	private Gastos gasto;
	
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@OneToOne
	@JoinColumn(name="caja_id")
	private Caja caja;


	public Cajachica() {
		this.fecha = new Date();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Double getSaldomp() {
		return saldomp;
	}
	public void setSaldomp(Double saldomp) {
		this.saldomp = saldomp;
	}
	public Double getSaldoefectivo() {
		return saldoefectivo;
	}
	public void setSaldoefectivo(Double saldoefectivo) {
		this.saldoefectivo = saldoefectivo;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public Factura getFactura() {
		return factura;
	}
	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	public Gastos getGasto() {
		return gasto;
	}
	public void setGasto(Gastos gasto) {
		this.gasto = gasto;
	}
	
	

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	@Override
	public String toString() {
		return "Cajachica [id=" + id + ", fecha=" + fecha + ", saldomp=" + saldomp + ", saldoefectivo=" + saldoefectivo
				+ ", monto=" + monto + ", factura=" + factura + ", gasto=" + gasto + "]";
	}
	
	

}
