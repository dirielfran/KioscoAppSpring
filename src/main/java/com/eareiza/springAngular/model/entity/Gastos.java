package com.eareiza.springAngular.model.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity //anotacion que identifica la clase como entidad
@Table(name="gastos") //nombre de la tabla en bd
public class Gastos {
	
	@Id //Anotacion que indica que el atributo es la clave primaria
	@GeneratedValue(strategy=GenerationType.IDENTITY)//Auto_increment MYSQL
	private Long id;
	private double montoPesos;
	private String descripcion;
	private Date fechaFact;
	private Date fechaCarga;
	private String usuario;
	private String proveedor;
	private String imagen= "+58kiosco.png";
	private String clasificacion;
	private double montoDolar;
	private double tasaDolar;
	
	private String metodopago;
	
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@OneToOne
	@JoinColumn(name="inventario_id")
	private Inventario inventario;

	@PrePersist
	@PreUpdate
	private void defaultValores() {
		this.fechaCarga = new Date();
//	    Authentication auth = SecurityContextHolder
//	            .getContext()
//	            .getAuthentication();
//	    UserDetails userDetail = (UserDetails) auth.getPrincipal();
//	    this.usuario = userDetail.getUsername();
//	    this.montoDolar = montoPesos / this.tasaDolar;
	}
	
	
	
	public String getMetodopago() {
		return metodopago;
	}
	public void setMetodopago(String metodopago) {
		this.metodopago = metodopago;
	}
	public Gastos() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getMontoPesos() {
		return montoPesos;
	}

	public void setMontoPesos(double monto) {
		this.montoPesos = monto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaFact() {
		return fechaFact;
	}

	public void setFechaFact(Date fechaFact) {
		this.fechaFact = fechaFact;
	}

	public Date getFechaCarga() {
		return fechaCarga;
	}
	
	public void setFechaCarga(Date fechaCarga) {
		this.fechaCarga = new Date();
	}
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public double getMontoDolar() {
		return montoDolar;
	}

	public void setMontoDolar(double dolar) {
		this.montoDolar = dolar;
	}

	public double getTasaDolar() {
		return tasaDolar;
	}

	public void setTasaDolar(double tasaDolar) {
		this.tasaDolar = tasaDolar;
	}
	
	

	public Inventario getInventario() {
		return inventario;
	}

	public void setInventario(Inventario inventario) {
		this.inventario = inventario;
	}

	@Override
	public String toString() {
		return "Gastos [id=" + id + ", montoPesos=" + montoPesos + ", descripcion=" + descripcion + ", fechaFact="
				+ fechaFact + ", fechaCarga=" + fechaCarga + ", usuario=" + usuario + ", proveedor=" + proveedor
				+ ", imagen=" + imagen + ", clasificacion=" + clasificacion + ", montoDolar=" + montoDolar
				+ ", tasaDolar=" + tasaDolar + ", inventario=" + inventario + "]";
	}

	

}
