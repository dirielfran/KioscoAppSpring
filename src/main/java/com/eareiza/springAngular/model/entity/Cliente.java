package com.eareiza.springAngular.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="clientes")
public class Cliente implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	//Se agrega validacion para que no este vacio
	@NotEmpty
	//Se agrega validacion de longitud
	@Size(min = 4, max = 12)
	//Se añade constrain para que el nombre no pueda ser null
	@Column(nullable = false)
	private String nombre;
	@NotEmpty
	private String apellido;
	
	//Se agrega validacion para que no ecepte vacios
	@NotEmpty
	//Se agrega validacion para el email formato
	@Email
	//Se añade constrain para que el nombre no pueda ser null y que sea unico
	@Column(nullable = false, unique = true)
	private String email;
	
	@NotNull(message = " no puede estar vacio.")
	@Column(name="create_at")
	//Para transformar a la fecha date de sql
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	private String foto;
	
	//@NotNull(message = "La region no puede ser vacia.")
	@ManyToOne
	@JoinColumn(name = "region_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Region region;
	
	//Se crea relacion con la clase Factura
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente", cascade = CascadeType.ALL)
	@JsonIgnoreProperties(value = {"cliente", "hibernateLazyInitializer", "handler"}, allowSetters = true)
	private List<Factura> facturas;	
	
	@JsonIgnoreProperties(value= {"cliente"}, allowSetters = true)
	@OneToMany(mappedBy = "cliente",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true )
	private List<RetiroCaja> retirosCaja;
	
	
	public Cliente() {
		this.facturas = new ArrayList<>();
		this.retirosCaja = new ArrayList<>();
	}
	
	public List<RetiroCaja> getRetirosCaja() {
		return retirosCaja;
	}
	
	//Se utiliza el metodo addPregunta para añadir la lsita de preguntas y seteo de examen
	public void setRetirosCaja(List<RetiroCaja> retiros) {
		this.retirosCaja.clear();
		retiros.forEach(this::addRetiro);
	}

	public List<Factura> getFacturas() {
		return facturas;
	}
	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}	
	
	
	public void addRetiro(RetiroCaja retiro ) {
		this.retirosCaja.add(retiro);
		//relacion inversa
		retiro.setCliente(this);
	}
	
	public void deleteRetiro(RetiroCaja retiro) {
		this.retirosCaja.remove(retiro);
		//Se estable relacion inversa al setearle null se elimana por la 
		//propiedad config. orphanRemoval = true
		retiro.setCliente(null);
	}
}
