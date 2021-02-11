package com.eareiza.springAngular.interfaces;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eareiza.springAngular.model.entity.Caja;

public interface ICajaService {

	List<Caja> findAll();
	Page<Caja> finAll(Pageable pagina);
	Caja findById(Long idCaja);
	Caja saveCaja(Caja caja);
	void deleteCaja(Long idCaja);
	Long countByEstadoCaja(String estado);
	Caja buscarXEstado(String estado);
}
