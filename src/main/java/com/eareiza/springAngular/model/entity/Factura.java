package com.eareiza.springAngular.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="facturas")
@JsonIgnoreProperties (ignoreUnknown = true)
public class Factura implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String descripcion;
	private String observacion;	
	private Double mercadopago;
	private Double pedidosya;
	private Double puntoventa;
	private Double montocomision;
	
	@Transient
	private Map<String, String> tipopago;
	
	private Double total;
	
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
		
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

	@PrePersist
	public void prePersistFecha() {
		this.createAt = new Date();
	}
	
	public Map<String, String> getTipopago() {
		return tipopago;
	}

	public void setTipopago(Map<String, String> tipopago) {
		this.tipopago = tipopago;
	}

	public Double getMontocomision() {
		return montocomision;
	}

	public void setMontocomision(Double montocomision) {
		this.montocomision = montocomision;
	}

	public Comision getComision() {
		return comision;
	}

	public void setComision(Comision comision) {
		this.comision = comision;
	}

	public Double getPedidosya() {
		return pedidosya;
	}

	public void setPedidosya(Double pedidosya) {
		this.pedidosya = pedidosya;
	}

	public Double getPuntoventa() {
		return puntoventa;
	}

	public void setPuntoventa(Double puntoventa) {
		this.puntoventa = puntoventa;
	}

	public Double getTotal() {
		
		return total;
	}		
	
	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getMercadopago() {
		return mercadopago;
	}

	public void setMercadopago(Double mercadopago) {
		this.mercadopago = mercadopago;
	}

	public List<ItemFactura> getItems() {
		return items;
	}

	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
		return "Factura [id=" + id + ", descripcion=" + descripcion + ", observacion=" + observacion + ", mercadopago="
				+ mercadopago + ", pedidosya=" + pedidosya + ", puntoventa=" + puntoventa + ", montocomision="
				+ montocomision + ", tipopago=" + tipopago + ", total=" + total + ", createAt=" + createAt
				+ ", cliente=" + cliente + ", caja=" + caja + ", items=" + items + ", comision=" + comision + "]";
	}
}
