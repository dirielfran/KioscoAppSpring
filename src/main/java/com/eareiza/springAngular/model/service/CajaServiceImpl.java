package com.eareiza.springAngular.model.service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eareiza.springAngular.interfaces.ICajaService;
import com.eareiza.springAngular.interfaces.ICajachicaService;
import com.eareiza.springAngular.model.entity.Caja;
import com.eareiza.springAngular.model.entity.Factura;
import com.eareiza.springAngular.model.entity.ItemFactura;
import com.eareiza.springAngular.model.entity.ItemInventario;
import com.eareiza.springAngular.model.repository.ICajaRepository;

// TODO: Auto-generated Javadoc
/**
 * The Class CajaServiceImpl.
 */
@Service
public class CajaServiceImpl implements ICajaService {

	/** The caja repo. */
	@Autowired
	private ICajaRepository cajaRepo;
	
	/** The caja service. */
	@Autowired
	private ICajachicaService cajaService;
	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Caja> findAll() {
		return cajaRepo.findAll();
	}

	/**
	 * Fin all.
	 *
	 * @param pagina the pagina
	 * @return the page
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<Caja> finAll(Pageable pagina) {
		return cajaRepo.findByOrderByIdDesc(pagina);
	}

	/**
	 * Find by id.
	 *
	 * @param idCaja the id caja
	 * @return the caja
	 */
	@Override
	@Transactional(readOnly = true)
	public Caja findById(Long idCaja) {
		return cajaRepo.findById(idCaja).orElse(null);
	}

	
	/**
	 * Guarda 
	 *
	 * @param Obj Caja
	 * @return obj Caja
	 */
	@Override
	@Transactional()
	public Caja saveCaja(Caja caja) {
		Double ganancias = 0D;
		//Recorre facturas de la caja
		for (Factura factura : caja.getFacturas()) {
			//Recorre items de cada factura
			for (ItemFactura item : factura.getItems()) {
				Double cantidad = item.getCantidad();
				Double precio = item.getPrecio();
				//Se recorren inventarios de cada item
				for (ItemInventario inventario : item.getItems_inventario()) {
					//Resta cantidades al inventario
					if(item.getItem_inventario() != null && item.getItem_inventario().getId() == inventario.getId()) {
						cantidad -= item.getCantinv();
						ganancias += item.getCantinv() * (precio - inventario.getPreciocompra());
					}else {
						ganancias += cantidad * (precio - inventario.getPreciocompra());
					}					
				}				
				ganancias += item.getComision();
			}
			//Se registra factura en la cajachica
			cajaService.registroCaja(factura);
		}
		caja.setGanancia(ganancias);
		return cajaRepo.save(caja);
	}

	/**
	 * Delete caja.
	 *
	 * @param idCaja the id caja
	 */
	@Override
	@Transactional()
	public void deleteCaja(Long idCaja) {
		cajaRepo.deleteById(idCaja);
	}

	/**
	 * Count by estado caja.
	 *
	 * @param estado the estado
	 * @return the long
	 */
	@Override
	public Long countByEstadoCaja(String estado) {
		return cajaRepo.countByEstado(estado);
	}

	/**
	 * Buscar X estado.
	 *
	 * @param estado the estado
	 * @return the caja
	 */
	@Override
	public Caja buscarXEstado(String estado) {
		return cajaRepo.findByEstado(estado);
	}

	/**
	 * Find diferencias X mes.
	 *
	 * @return the double
	 */
	/*
	 * Metodo que obtiene las diferencias del mes actual
	 * */
	@Override
	public Double findDiferenciasXMes() {
		LocalDate fecha = LocalDate.now();
		LocalDate desde = fecha.withDayOfMonth(1);
		LocalDate hasta = fecha.withDayOfMonth(fecha.lengthOfMonth());
		Object obj = cajaRepo.findDiferenciasXMes(desde, hasta);
		Double perdidas = (Double) obj;
		return perdidas;
	}

}
