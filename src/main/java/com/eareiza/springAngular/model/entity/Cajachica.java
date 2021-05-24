package com.eareiza.springAngular.model.entity;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cajachica")
public class Cajachica extends EntityCommon implements Serializable{

	private Double saldomp;
	private Double saldopy;
	private Double saldopv;
	private Double saldoefectivopy;
	private Double saldoefectivo;
	private Double monto;
	private Boolean transferencia;
	
	
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

	/**
	 *
	 */
	private static final long serialVersionUID = -8134548246055327266L;
}
