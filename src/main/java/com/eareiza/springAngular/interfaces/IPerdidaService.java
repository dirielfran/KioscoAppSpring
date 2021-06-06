package com.eareiza.springAngular.interfaces;

import java.util.List;


import com.eareiza.springAngular.DTO.PerdidaDto;
import com.eareiza.springAngular.model.entity.Perdida;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;

public interface IPerdidaService {

	List<Perdida> findAll();
	Page<PerdidaDto> finAll(Integer pagina) throws NotFoundException;
	Perdida findById(Long idPerdida);
	Perdida savePerdida(Perdida perdida);
	void deletePerdida(Long idPerdida);
	public Double findPerdidasXMes();
	
}
