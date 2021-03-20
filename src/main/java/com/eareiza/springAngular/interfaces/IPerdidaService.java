package com.eareiza.springAngular.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eareiza.springAngular.model.entity.Perdida;

public interface IPerdidaService {

	List<Perdida> findAll();
	Page<Perdida> finAll(Pageable pagina);
	Perdida findById(Long idPerdida);
	Perdida savePerdida(Perdida perdida);
	void deletePerdida(Long idPerdida);
	public Double findPerdidasXMes();
	
}
