package com.eareiza.springAngular.model.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="inventarios_items")
public class ItemInventario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechacreate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechamod;
	
	//Se mapea la relacion con tabla Producto
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "producto_id")
	private Producto producto;
	
	private String estado = "Activo";
	
	private Double stockinicial;
	private Double stockadd;
	private Double existencia;
	
	private Double precioventa;
	private Double preciocompra;
	
	private Boolean consignacion = false;
	
	@PrePersist
	private void prePersist() {
		this.fechacreate = new Date();
	}
	
	@PreUpdate
	private void preUpdate() {
		this.fechamod = new Date();
	}	
	
	public Boolean getConsignacion() {
		return consignacion;
	}

	public void setConsignacion(Boolean consignacion) {
		this.consignacion = consignacion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFechacreate() {
		return fechacreate;
	}

	public void setFechacreate(Date fechacreate) {
		this.fechacreate = fechacreate;
	}

	public Date getFechamod() {
		return fechamod;
	}

	public void setFechamod(Date fechamod) {
		this.fechamod = fechamod;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Double getStockadd() {
		return stockadd;
	}

	public void setStockadd(Double stockadd) {
		this.stockadd = stockadd;
	}
	
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Double getStockinicial() {
		return stockinicial;
	}
	
	public void setStockinicial(Double stockinicial) {
		this.stockinicial = stockinicial;
	}

	public Double getExistencia() {
		return existencia;
	}

	public void setExistencia(Double existencia) {
		this.existencia = existencia;
	}

	public Double getPrecioventa() {
		return precioventa;
	}
	public void setPrecioventa(Double precioventa) {
		this.precioventa = precioventa;
	}

	public Double getPreciocompra() {
		return preciocompra;
	}

	public void setPreciocompra(Double preciocompra) {
		this.preciocompra = preciocompra;
	}
}
