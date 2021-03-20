package com.eareiza.springAngular.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Table(name="perdidas")
@Entity
public class Perdida implements Serializable{

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "create_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createAt;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@JoinColumn(name = "user")
	private String user;
	
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@OneToOne
	@JoinColumn(name="producto_id")
	private Producto producto;
	
	private String tipo;
	
	private Double cantidad;
	
	private Double precio;
	
	private Double monto;
	
	private String Descripcion;
	
	// Se mapea la relacion con tabla ItemInventario
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iteminventario_id")
	private ItemInventario item_inventario;
	
	private Double cantinv;
	
	// Se le añade el cascade para que se elimine en cascada cuando sea eliminada la
	// entidad
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "itemsperdida_itemsinventario", joinColumns = @JoinColumn(name = "itemperdida_id"), inverseJoinColumns = @JoinColumn(name = "iteminventario_id"),
			// Se configura para que exista una key usuario rol igual
			uniqueConstraints = { @UniqueConstraint(columnNames = { "itemperdida_id", "iteminventario_id" }) })
	private List<ItemInventario> items_inventario;
	
	@PrePersist
	public void prePersistFecha() {
		this.createAt = new Date();
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

	public List<ItemInventario> getItems_inventario() {
		return items_inventario;
	}
	public void setItems_inventario(List<ItemInventario> items_inventario) {
		this.items_inventario = items_inventario;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public String getDescripcion() {
		return Descripcion;
	}
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}

	

	@Override
	public String toString() {
		return "Perdida [id=" + id + ", createAt=" + createAt + ", user=" + user + ", producto=" + producto + ", tipo="
				+ tipo + ", cantidad=" + cantidad + ", precio=" + precio + ", monto=" + monto + ", Descripcion="
				+ Descripcion + ", item_inventario=" + item_inventario + ", cantinv=" + cantinv + ", items_inventario="
				+ items_inventario + "]";
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1371542177788588449L;

}
