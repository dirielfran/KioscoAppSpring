package com.eareiza.springAngular.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eareiza.springAngular.interfaces.ICajachicaService;
import com.eareiza.springAngular.model.entity.Caja;
import com.eareiza.springAngular.model.entity.Cajachica;
import com.eareiza.springAngular.model.entity.Factura;
import com.eareiza.springAngular.model.entity.Gastos;
import com.eareiza.springAngular.model.repository.ICajaChicaRepository;

// TODO: Auto-generated Javadoc
/**
 * The Class CajachicaServiceImpl.
 */
@Service
public class CajachicaServiceImpl implements ICajachicaService{
	
	/** The caja repo. */
	@Autowired
	ICajaChicaRepository cajaRepo;

	/**
	 * Find top by order by id desc.
	 *
	 * @return the cajachica
	 */
	@Override
	public Cajachica findTopByOrderByIdDesc() {
		return cajaRepo.findTopByOrderByIdDesc();
	}
	
	
	/**
	 * Registro caja.
	 *
	 * @param factura the factura
	 */
	public void registroCaja(Factura factura) {
		//Busca el ultimo registro de Cajahica
		Cajachica cajaOld = cajaRepo.findTopByOrderByIdDesc();
		Cajachica caja = new Cajachica();
		caja.setFactura(factura);
		caja.setMonto(factura.getTotal());
		//Tomo saldos de la factura
		Double mercadopago = factura.getMercadopago() != null ? factura.getMercadopago() : 0;
		Double totalFactura = factura.getTotal() != null ? factura.getTotal() : 0;
		//Creo nuevos refistros
		caja.setSaldomp(cajaOld.getSaldomp()+mercadopago);
		caja.setSaldoefectivo(cajaOld.getSaldoefectivo()+(totalFactura-mercadopago));
		cajaRepo.save(caja);
	}
	
	/**
	 * Registro diferencia caja.
	 *
	 * @param caja the caja
	 * @param diferencia the diferencia
	 */
	public void registroDiferenciaCaja(Caja caja,Double diferencia) {
		Cajachica cajaOld = cajaRepo.findTopByOrderByIdDesc();
		Cajachica cajachica = new Cajachica();
		cajachica.setMonto(diferencia);
		cajachica.setCaja(caja);
		cajachica.setSaldomp(cajaOld.getSaldomp());
		cajachica.setSaldoefectivo(cajaOld.getSaldoefectivo()+diferencia);
		cajaRepo.save(cajachica);
	}
	
	
	
	/**
	 * Registro caja.
	 *
	 * @param gasto the gasto
	 */
	public void registroCaja(Gastos gasto) {
		Cajachica cajaOld = cajaRepo.findTopByOrderByIdDesc();
		Cajachica caja = new Cajachica();
		caja.setGasto(gasto);
		caja.setMonto(gasto.getMontoPesos());
		if(gasto.getMetodopago().equals("Efectivo")) {
			caja.setSaldoefectivo(cajaOld.getSaldoefectivo()-gasto.getMontoPesos());
			caja.setSaldomp(cajaOld.getSaldomp());
		}else {
			caja.setSaldomp(cajaOld.getSaldomp()-gasto.getMontoPesos());
			caja.setSaldoefectivo(cajaOld.getSaldoefectivo());
		}
		cajaRepo.save(caja);
	}
	
	/**
	 * Delete registro caja.
	 *
	 * @param factura the factura
	 */
	public void deleteRegistroCaja(Factura factura) {
		Double mercadopago = factura.getMercadopago() != null ? factura.getMercadopago() : 0;
		Double totalFactura = factura.getTotal() != null ? factura.getTotal() : 0;
		Cajachica cajaOld = cajaRepo.findTopByOrderByIdDesc();
		Cajachica caja = new Cajachica();
		caja.setMonto(factura.getTotal()*(-1));
		caja.setSaldomp(cajaOld.getSaldomp()-mercadopago);
		caja.setSaldoefectivo(cajaOld.getSaldoefectivo()-(totalFactura-mercadopago));
		cajaRepo.save(caja);
	}
	
	/**
	 * Delete registro caja.
	 *
	 * @param gasto the gasto
	 */
	public void deleteRegistroCaja(Gastos gasto) {
		Cajachica cajaOld = cajaRepo.findTopByOrderByIdDesc();
		Cajachica caja = new Cajachica();
		caja.setMonto(gasto.getMontoPesos()*(-1));
		if(gasto.getMetodopago().equals("Efectivo")) {
			caja.setSaldoefectivo(cajaOld.getSaldoefectivo()+gasto.getMontoPesos());
			caja.setSaldomp(cajaOld.getSaldomp());
		}else {
			caja.setSaldomp(cajaOld.getSaldomp()+gasto.getMontoPesos());
			caja.setSaldoefectivo(cajaOld.getSaldoefectivo());
		}
		cajaRepo.save(caja);
	}

	/**
	 * Save caja.
	 *
	 * @param caja the caja
	 * @return the cajachica
	 */
	@Override
	public Cajachica saveCaja(Cajachica caja) {
		return cajaRepo.save(caja);
	}

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the cajachica
	 */
	@Override
	public Cajachica findById(Long id) {
		return cajaRepo.findById(id).orElse(null);
	}

	/**
	 * Find by factura.
	 *
	 * @param id the id
	 * @return the cajachica
	 */
	@Override
	public Cajachica findByFactura(Long id) {
		return cajaRepo.findByfacturaId(id);
	}

	/**
	 * Find by gasto.
	 *
	 * @param id the id
	 * @return the cajachica
	 */
	@Override
	public Cajachica findByGasto(Long id) {
		return cajaRepo.findByGastoId(id);
	}

	/**
	 * Delete caja.
	 *
	 * @param id the id
	 */
	@Override
	public void deleteCaja(Long id) {
		cajaRepo.deleteById(id);
	}		
}
