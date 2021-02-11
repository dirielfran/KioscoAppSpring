package com.eareiza.springAngular.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eareiza.springAngular.interfaces.IRetiroCajaService;
import com.eareiza.springAngular.model.entity.RetiroCaja;
import com.eareiza.springAngular.model.repository.IRetiroCajaRepository;

@Service
public class RetiroCajaServiceImpl implements IRetiroCajaService {
	
	@Autowired
	IRetiroCajaRepository retiroCajaRepo;

	@Override
	public RetiroCaja getbyId(Long idRetiroCaja) {
		return retiroCajaRepo.findById(idRetiroCaja).orElse(null);
	}

	@Override
	public List<RetiroCaja> getRetirosCaja() {
		return (List<RetiroCaja>) retiroCajaRepo.findAll();
	}

	@Override
	public RetiroCaja saveRetiroCaja(RetiroCaja retiro) {
		return retiroCajaRepo.save(retiro);
	}

	@Override
	public void deleteRetiroCaja(Long idRetiro) {
		retiroCajaRepo.deleteById(idRetiro);
	}
}
