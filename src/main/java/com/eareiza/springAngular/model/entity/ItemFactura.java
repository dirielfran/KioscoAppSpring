package com.eareiza.springAngular.model.entity;

import java.io.Serializable;
import java.util.List;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "facturas_items")
@JsonIgnoreProperties (ignoreUnknown = true)
public class ItemFactura extends EntityCommon implements Serializable {

	private Double cantidad;
	private Double precio;
	private Double total;

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

	// Se le añade el cascade para que se elimine en cascada cuando sea eliminada la
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

	private static final long serialVersionUID = 1L;
}
