package com.eareiza.springAngular.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="productos")
@SQLDelete(sql = "UPDATE productos SET deleted=true WHERE id = ?")
@Where(clause = "deleted = false")
public class Producto extends EntityCommon implements Serializable{

	private String nombre;
	private Double precio;
	private Double existencia ;
	private String proveedor;
	private Double minimo;
	private Double preciocosto;
	
	@Column(name="deleted")
    private Boolean deleted = Boolean.FALSE;


	@PrePersist
	public void prePersistFecha() {
		this.existencia = 0D;
	}
	private static final long serialVersionUID = 1L;
}
