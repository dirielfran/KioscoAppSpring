package com.eareiza.springAngular.model.entity;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name="inventarios_items")
public class ItemInventario extends EntityCommon{

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
}
