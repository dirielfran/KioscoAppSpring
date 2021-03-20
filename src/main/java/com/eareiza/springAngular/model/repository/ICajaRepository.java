package com.eareiza.springAngular.model.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eareiza.springAngular.model.entity.Caja;

public interface ICajaRepository extends JpaRepository<Caja, Long> {
	
	public Long countByEstado(String estado);
	public Caja findByEstado(String estado);
	public Page<Caja> findByOrderByIdDesc(Pageable page);
	
	//Suma de las diferencias en las cajas por mes
	@Query(value="select SUM(c.diferencia) from cajas c where c.estado = 'Cerrado' and c.fechaopen between ?1 and ?2", nativeQuery = true)
	public Object findDiferenciasXMes(LocalDate fechIni, LocalDate fechaFin);

}
