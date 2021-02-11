package com.eareiza.springAngular.model.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.eareiza.springAngular.model.entity.Cajachica;

public interface ICajaChicaRepository extends CrudRepository<Cajachica, Long> {
	
	Cajachica findTopByOrderByIdDesc();
	
	@Query("select c from Cajachica c where factura.id = ?1")
	Cajachica findByfacturaId(Long idFactura);
	
	@Query("select c from Cajachica c where gasto.id = ?1")
	Cajachica findByGastoId(Long idGasto);

}
