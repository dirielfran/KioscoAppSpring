package com.eareiza.springAngular.interfaces;

import java.util.Map;

import com.eareiza.springAngular.model.entity.Caja;
import com.eareiza.springAngular.model.entity.Cajachica;
import com.eareiza.springAngular.model.entity.Factura;
import com.eareiza.springAngular.model.entity.Gastos;

public interface ICajachicaService {
	
	public Cajachica findTopByOrderByIdDesc();
	public void registroCaja(Gastos gasto);
	public void registroCaja(Factura factura);
	public void registroDiferenciaCaja(Caja caja,Double diferencia);
	public void deleteRegistroCaja(Factura factura);
	public void deleteRegistroCaja(Gastos gasto);
	public Cajachica saveCaja(Cajachica caja);
	public Cajachica findById(Long id);
	public Cajachica findByFactura(Long id);
	public Cajachica findByGasto(Long id);
	public void deleteCaja(Long id);
	public void transaferenciaSaldo(Map<String, String> mapa);
	
}
