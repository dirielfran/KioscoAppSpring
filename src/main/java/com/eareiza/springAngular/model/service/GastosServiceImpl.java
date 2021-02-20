package com.eareiza.springAngular.model.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eareiza.springAngular.DTO.ConsignacionDto;
import com.eareiza.springAngular.interfaces.ICajachicaService;
import com.eareiza.springAngular.interfaces.IGastosService;
import com.eareiza.springAngular.model.entity.Cajachica;
import com.eareiza.springAngular.model.entity.Gastos;
import com.eareiza.springAngular.model.entity.Inventario;
import com.eareiza.springAngular.model.repository.GastosRepository;

@Service
public class GastosServiceImpl implements IGastosService{


	@Autowired
	GastosRepository repoGastos;
	
	@Autowired
	private ICajachicaService cajaService;
	

	public List<Gastos> buscarTodos() {
		return repoGastos.findAll();
	}

	public Gastos buscarPorId(Long id) {
		Optional<Gastos> gasto = repoGastos.findById(id);
		if (gasto.isPresent()) {
			return gasto.get();
		}
		return null;
	}

	public List<Gastos> buscarPorFecha(Date fechaGasto) {
		return repoGastos.findByFechaFact(fechaGasto);
	}

	public List<Gastos> buscarPorUsuario(String Usuario) {
		return repoGastos.findByUsuario(Usuario);
	}

	public Gastos guardar(Gastos gasto) {
		//Se registra factura en la cajachica
		Gastos gastoNew = repoGastos.save(gasto);
		cajaService.registroCaja(gasto);
		return gastoNew;
	}
	
	public void crearGastoInventario(Inventario inventario, String tipo, ConsignacionDto consignacion, Boolean mercadoPago) {

		//Se añade gasto
		Gastos gasto = new Gastos();
		Double monto = consignacion == null ?inventario.getTotal() : consignacion.getCantidad()*consignacion.getPrecio();
		gasto.setMontoPesos(monto);
		gasto.setDescripcion(inventario.getDescripcion());
		gasto.setFechaFact(new Date());
		gasto.setUsuario(inventario.getUsuario().getUsername());
		gasto.setProveedor(inventario.getProveedor());
		gasto.setClasificacion(tipo);
		gasto.setInventario(inventario);
		if(mercadoPago != null ) {
			inventario.setMetodopago(mercadoPago?"MercadoPago":"Efectivo");
		}
		gasto.setMetodopago(((inventario.getMetodopago()!=null) ? inventario.getMetodopago() : !mercadoPago ? "Efectivo" : "MercadoPago"));
		
		//Se crea el gasto
		guardar(gasto);
	}

	public void borrarGasto(Long id) {
		Gastos gasto = buscarPorId(id);
		//Se elimina registro de factura en caja
		cajaService.deleteRegistroCaja(gasto);
		Cajachica caja = cajaService.findByGasto(gasto.getId());
		cajaService.deleteCaja(caja.getId());
		repoGastos.deleteById(id);
	}

	public List<Gastos> buscarPorClasificacion(String clasificacion) {
		return repoGastos.findByClasificacion(clasificacion);
	}

	@Override
	public Page<Gastos> getGastos(Pageable pagina) {
		return repoGastos.findAll(pagina);
	}

	@Override
	public Gastos buscarPorInventario(Long inventario) {
		return repoGastos.findByInventario(inventario);
	}

	/*
	 * Metodo que obtiene las ganancias del mes actual
	 * */
	@Override
	public Double findGanancias() {
		LocalDate fecha = LocalDate.now();
		LocalDate desde = fecha.withDayOfMonth(1);
		LocalDate hasta = fecha.withDayOfMonth(fecha.lengthOfMonth());
		Object obj = repoGastos.findGananciasXMes(desde, hasta);
		Double ganancias = (Double) obj;
		return ganancias;
	}
	
	/*
	 * Metodo que obtiene los gastos del mes actual
	 * */
	@Override
	public Double findGastosxmes() {
		LocalDate fecha = LocalDate.now();
		LocalDate desde = fecha.withDayOfMonth(1);
		LocalDate hasta = fecha.withDayOfMonth(fecha.lengthOfMonth());
		Object obj = repoGastos.findGastosXMes("Gasto", desde, hasta);
		Double gastos = (Double) obj;
		return gastos;
	}

}
