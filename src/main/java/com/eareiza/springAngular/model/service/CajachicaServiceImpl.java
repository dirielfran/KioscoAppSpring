package com.eareiza.springAngular.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eareiza.springAngular.interfaces.ICajachicaService;
import com.eareiza.springAngular.model.entity.Caja;
import com.eareiza.springAngular.model.entity.Cajachica;
import com.eareiza.springAngular.model.entity.Factura;
import com.eareiza.springAngular.model.entity.Gastos;
import com.eareiza.springAngular.model.repository.ICajaChicaRepository;

@Service
public class CajachicaServiceImpl implements ICajachicaService{
	
	@Autowired
	ICajaChicaRepository cajaRepo;

	@Override
	public Cajachica findTopByOrderByIdDesc() {
		return cajaRepo.findTopByOrderByIdDesc();
	}
	
	public void registroCaja(Factura factura) {
		Cajachica cajaOld = cajaRepo.findTopByOrderByIdDesc();
		Cajachica caja = new Cajachica();
		caja.setFactura(factura);
		caja.setMonto(factura.getTotal());
		Double mercadopago = factura.getMercadopago() != null ? factura.getMercadopago() : 0;
		Double totalFactura = factura.getTotal() != null ? factura.getTotal() : 0;
		caja.setSaldomp(cajaOld.getSaldomp()+mercadopago);
		caja.setSaldoefectivo(cajaOld.getSaldoefectivo()+(totalFactura-mercadopago));
		cajaRepo.save(caja);
	}
	
	public void registroDiferenciaCaja(Caja caja,Double diferencia) {
		Cajachica cajaOld = cajaRepo.findTopByOrderByIdDesc();
		Cajachica cajachica = new Cajachica();
		cajachica.setMonto(diferencia);
		cajachica.setCaja(caja);
		cajachica.setSaldomp(cajaOld.getSaldomp());
		cajachica.setSaldoefectivo(cajaOld.getSaldoefectivo()+diferencia);
		cajaRepo.save(cajachica);
	}
	
	
	
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

	@Override
	public Cajachica saveCaja(Cajachica caja) {
		return cajaRepo.save(caja);
	}

	@Override
	public Cajachica findById(Long id) {
		return cajaRepo.findById(id).orElse(null);
	}

	@Override
	public Cajachica findByFactura(Long id) {
		return cajaRepo.findByfacturaId(id);
	}

	@Override
	public Cajachica findByGasto(Long id) {
		return cajaRepo.findByGastoId(id);
	}

	@Override
	public void deleteCaja(Long id) {
		cajaRepo.deleteById(id);
	}		
}
