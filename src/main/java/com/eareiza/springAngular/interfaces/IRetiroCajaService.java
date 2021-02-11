package com.eareiza.springAngular.interfaces;

import java.util.List;

import com.eareiza.springAngular.model.entity.RetiroCaja;

public interface IRetiroCajaService {
	RetiroCaja getbyId(Long idRetiroCaja);
	List<RetiroCaja> getRetirosCaja();
	RetiroCaja saveRetiroCaja(RetiroCaja retiro);
	void deleteRetiroCaja(Long idRetiro);
}
