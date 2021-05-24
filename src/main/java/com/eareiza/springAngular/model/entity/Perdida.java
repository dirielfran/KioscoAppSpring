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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="perdidas")
@Entity
public class Perdida extends EntityCommon implements Serializable{
	
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1371542177788588449L;

}
