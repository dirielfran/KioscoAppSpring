package com.eareiza.springAngular.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	/** The caja repository. */
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
	 * Metodo registroCaja. registra la factura en la caja chica afectando los saldos
	 *
	 * @param factura. Objeto de tipo Factura, a registran en los saldos.
	 */
	public void registroCaja(Factura factura) {
		//TODO trabajar con excepciones
		Double mercadopago = factura.getMercadopago() != null ? factura.getMercadopago() : 0;
		Double pedidosYa = factura.getPedidosya() != null ? factura.getPedidosya() : 0;
		Double puntoVenta = factura.getPuntoventa() != null ? factura.getPuntoventa() : 0;
		Double totalFactura = factura.getTotal() != null ? factura.getTotal() : 0;
		Cajachica cajaOld = cajaRepo.findTopByOrderByIdDesc();
		Cajachica caja = new Cajachica();
		caja.setFactura(factura);
		caja.setMonto(totalFactura);
		caja.setSaldomp(cajaOld.getSaldomp()+mercadopago);
		caja.setSaldopy(cajaOld.getSaldopy()+pedidosYa);
		caja.setSaldopv(cajaOld.getSaldopv()+puntoVenta);
		caja.setSaldoefectivo(cajaOld.getSaldoefectivo()+(totalFactura - mercadopago - pedidosYa - puntoVenta));
		cajaRepo.save(caja);
	}
	
	/**
	 * Registro diferencia caja. Mtodo que registra en caja chica las diferenciaas en 
	 * efectivo al cerrar una caja
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
		cajachica.setSaldopv(cajaOld.getSaldopv());
		cajachica.setSaldopy(cajaOld.getSaldopy());
		cajachica.setSaldoefectivo(cajaOld.getSaldoefectivo()+diferencia);
		cajaRepo.save(cajachica);
	}
	
	
	
	/**
	 * Registro caja. Registro de gastos en caja chica
	 *
	 * @param gasto. Objeto de tipo gasto que se registr en cajachica
	 */
	public void registroCaja(Gastos gasto) {
		Cajachica cajaOld = cajaRepo.findTopByOrderByIdDesc();
		Cajachica caja = new Cajachica();
		caja.setGasto(gasto);
		caja.setMonto(gasto.getMontoPesos());
		if(gasto.getMetodopago().equals("Efectivo")) {
			caja.setSaldoefectivo(cajaOld.getSaldoefectivo()-gasto.getMontoPesos());
			caja.setSaldomp(cajaOld.getSaldomp());
			caja.setSaldopy(cajaOld.getSaldopy());
			caja.setSaldopv(cajaOld.getSaldopv());
		}else {
			caja.setSaldomp(cajaOld.getSaldomp()-gasto.getMontoPesos());
			caja.setSaldoefectivo(cajaOld.getSaldoefectivo());
			caja.setSaldopy(cajaOld.getSaldopy());
			caja.setSaldopv(cajaOld.getSaldopv());
		}
		cajaRepo.save(caja);
	}
	
	/**
	 * Metodo deleteRegistroCaja. resta los saldos de la factura eliminada 
	 *
	 * @param factura. Objeto tipo Factura que se elimina
	 */
	public void deleteRegistroCaja(Factura factura) {
		Double mercadopago = factura.getMercadopago() != null ? factura.getMercadopago() : 0;
		Double pedidosYa   = factura.getPedidosya() != null ? factura.getPedidosya() : 0;
		Double puntoVenta  = factura.getPuntoventa() != null ? factura.getPuntoventa() : 0; 
		Double totalFactura = factura.getTotal() != null ? factura.getTotal() : 0;
		Cajachica cajaOld = cajaRepo.findTopByOrderByIdDesc();
		Cajachica caja = new Cajachica();
		//TODO Validar con excepciones los saldos
		caja.setMonto(factura.getTotal()*(-1));
		caja.setSaldomp(cajaOld.getSaldomp() - mercadopago);
		caja.setSaldopv(cajaOld.getSaldopv() - puntoVenta);
		caja.setSaldopy(cajaOld.getSaldopy() - pedidosYa);
		caja.setSaldoefectivo(cajaOld.getSaldoefectivo()-(totalFactura - mercadopago - pedidosYa - puntoVenta));
		cajaRepo.save(caja);
	}
	
	/**
	 * Metodo deleteRegistroCaja. Metodo que calcula y registra los saldos al cargar
	 * un gasto en sistema
	 *
	 * @param gasto. Obj de tipo Gasto que se eliminara 
	 */
	public void deleteRegistroCaja(Gastos gasto) {
		Cajachica cajaOld = cajaRepo.findTopByOrderByIdDesc();
		Cajachica caja = new Cajachica();
		caja.setMonto(gasto.getMontoPesos()*(-1));
		if(gasto.getMetodopago().equals("Efectivo")) {
			caja.setSaldoefectivo(cajaOld.getSaldoefectivo()+gasto.getMontoPesos());
			caja.setSaldomp(cajaOld.getSaldomp());
			caja.setSaldopy(cajaOld.getSaldopy());
			caja.setSaldopv(cajaOld.getSaldopv());
		}else {
			caja.setSaldomp(cajaOld.getSaldomp()+gasto.getMontoPesos());
			caja.setSaldoefectivo(cajaOld.getSaldoefectivo());
			caja.setSaldopy(cajaOld.getSaldopy());
			caja.setSaldopv(cajaOld.getSaldopv());
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
	
	@Transactional()
	public void transaferenciaSaldo(Map<String, String> mapa) {
		Cajachica cajachica = cajaRepo.findTopByOrderByIdDesc();
		Cajachica newCajaChica = new Cajachica();
		Double monto = Double.valueOf(mapa.get("monto"));
		newCajaChica.setMonto(monto);
		newCajaChica.setTransferencia(true);
		switch (mapa.get("origen")) {
		case "Efectivo":
			origenEF(mapa.get("destino"), newCajaChica, cajachica, monto);
			break;
		case "MercadoPago":
			origenMP(mapa.get("destino"), newCajaChica, cajachica, monto);
			break;
		case "PedidosYa":
			origenPY(mapa.get("destino"), newCajaChica, cajachica, monto);
			break;
		case "PuntoVenta":
			origenPV(mapa.get("destino"), newCajaChica, cajachica, monto);
			break;
		default:
			break;
		}
		cajaRepo.save(newCajaChica);
	}
	
	private void origenEF(String destino,Cajachica newCajaChica, Cajachica cajachica, Double monto) {
		newCajaChica.setSaldoefectivo(cajachica.getSaldoefectivo() - monto);
		switch (destino) {
		case "MercadoPago":
			newCajaChica.setSaldomp(cajachica.getSaldomp() + monto);
			newCajaChica.setSaldopv(cajachica.getSaldopv());
			newCajaChica.setSaldopy(cajachica.getSaldopy());
			break;
		case "PedidosYa":
			newCajaChica.setSaldopy(cajachica.getSaldopy() + monto);
			newCajaChica.setSaldopv(cajachica.getSaldopv());
			newCajaChica.setSaldomp(cajachica.getSaldomp());
			break;
		case "PuntoVenta":
			newCajaChica.setSaldopv(cajachica.getSaldopv() + monto);
			newCajaChica.setSaldopy(cajachica.getSaldopy());
			newCajaChica.setSaldomp(cajachica.getSaldomp());
		default:
			break;
		}
	}
	
	private void origenMP(String destino, Cajachica newCajaChica, Cajachica cajachica, Double monto) {
		newCajaChica.setSaldomp(cajachica.getSaldomp() - monto);
		switch (destino) {
		case "Efectivo":
			newCajaChica.setSaldoefectivo(cajachica.getSaldoefectivo() + monto);
			newCajaChica.setSaldopv(cajachica.getSaldopv());
			newCajaChica.setSaldopy(cajachica.getSaldopy());
			break;
		case "PedidosYa":
			newCajaChica.setSaldopy(cajachica.getSaldopy() + monto);
			newCajaChica.setSaldopv(cajachica.getSaldopv());
			newCajaChica.setSaldoefectivo(cajachica.getSaldoefectivo());
			break;
		case "PuntoVenta":
			newCajaChica.setSaldopv(cajachica.getSaldopv() + monto);
			newCajaChica.setSaldopy(cajachica.getSaldopy());
			newCajaChica.setSaldoefectivo(cajachica.getSaldoefectivo());
		default:
			break;
		}
	}
	
	private void origenPY(String destino, Cajachica newCajaChica, Cajachica cajachica, Double monto) {
		newCajaChica.setSaldopy(cajachica.getSaldopy() - monto);
		switch (destino) {
		case "Efectivo":
			newCajaChica.setSaldoefectivo(cajachica.getSaldoefectivo() + monto);
			newCajaChica.setSaldomp(cajachica.getSaldomp());
			newCajaChica.setSaldopv(cajachica.getSaldopv());
			break;
		case "MercadoPago":
			newCajaChica.setSaldomp(cajachica.getSaldomp() + monto);
			newCajaChica.setSaldoefectivo(cajachica.getSaldoefectivo());
			newCajaChica.setSaldopv(cajachica.getSaldopv());
			break;
		case "PuntoVenta":
			newCajaChica.setSaldopv(cajachica.getSaldopv() + monto);
			newCajaChica.setSaldomp(cajachica.getSaldomp());
			newCajaChica.setSaldoefectivo(cajachica.getSaldoefectivo());
		default:
			break;
		}
	}
	
	private void origenPV(String destino, Cajachica newCajaChica, Cajachica cajachica, Double monto) {
		newCajaChica.setSaldopv(cajachica.getSaldopv() - monto);
		switch (destino) {
		case "Efectivo":
			newCajaChica.setSaldoefectivo(cajachica.getSaldoefectivo() + monto);
			newCajaChica.setSaldomp(cajachica.getSaldomp());
			newCajaChica.setSaldopy(cajachica.getSaldopy());
			break;
		case "MercadoPago":
			newCajaChica.setSaldomp(cajachica.getSaldomp() + monto);
			newCajaChica.setSaldoefectivo(cajachica.getSaldoefectivo());
			newCajaChica.setSaldopy(cajachica.getSaldopy());
			break;
		case "PedidosYa":
			newCajaChica.setSaldopy(cajachica.getSaldopy() + monto);
			newCajaChica.setSaldomp(cajachica.getSaldomp());
			newCajaChica.setSaldoefectivo(cajachica.getSaldoefectivo());
		default:
			break;
		}
	}	
}
