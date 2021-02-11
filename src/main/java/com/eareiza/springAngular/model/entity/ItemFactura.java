package com.eareiza.springAngular.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "facturas_items")
@JsonIgnoreProperties (ignoreUnknown = true)
public class ItemFactura implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double cantidad;

	private Double precio;

	// Se mapea la relacion con tabla Producto
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "producto_id")
	private Producto producto;

	// Se mapea la relacion con tabla Producto
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iteminventario_id")
	private ItemInventario item_inventario;

	// Se le añade el cascade para que se elimine en cascda cuando sea eliminada la
	// entidad
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "itemsfactura_itemsinventario", joinColumns = @JoinColumn(name = "itemfactura_id"), inverseJoinColumns = @JoinColumn(name = "iteminventario_id"),
			// Se configura para que exista una key usuario rol igual
			uniqueConstraints = { @UniqueConstraint(columnNames = { "itemfactura_id", "iteminventario_id" }) })
	private List<ItemInventario> items_inventario;

	private Double cantinv;

	private Double comision;
	
	
	private Boolean consignacion = false;
	
	public Boolean getConsignacion() {
		return consignacion;
	}

	public void setConsignacion(Boolean consignacion) {
		this.consignacion = consignacion;
	}

	public Double getComision() {
		return comision;
	}

	public void setComision(Double comision) {
		this.comision = comision;
	}

	public ItemInventario getItem_inventario() {
		return item_inventario;
	}

	public void setItem_inventario(ItemInventario item_inventario) {
		this.item_inventario = item_inventario;
	}

	public Double getCantinv() {
		return cantinv;
	}

	public void setCantinv(Double cantinv) {
		this.cantinv = cantinv;
	}

	public Double getImporte() {
		return cantidad.doubleValue() * producto.getPrecio();
	}

	public List<ItemInventario> getItems_inventario() {
		return items_inventario;
	}

	public void setItems_inventario(List<ItemInventario> items_inventario) {
		this.items_inventario = items_inventario;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	private static final long serialVersionUID = 1L;
	
	

}
