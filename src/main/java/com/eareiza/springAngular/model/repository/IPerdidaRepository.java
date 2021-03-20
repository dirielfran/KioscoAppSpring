package com.eareiza.springAngular.model.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eareiza.springAngular.model.entity.Perdida;

@Repository
public interface IPerdidaRepository extends JpaRepository<Perdida, Long> {
	
	public Page<Perdida> findByOrderByIdDesc(Pageable page);
	
	@Query(value="select SUM(p.monto) from perdidas p where p.create_at between ?1 and ?2", nativeQuery = true)
	public Object findPerdidasXMes(LocalDate fechIni, LocalDate fechaFin);

}
