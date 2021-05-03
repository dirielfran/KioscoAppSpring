package com.eareiza.springAngular.model.repository;

import java.time.LocalDate;
import java.util.List;

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
	
	@Query(value="SELECT SUM(monto) as perdidas, CONCAT(YEAR(create_at), '/', WEEK(create_at)) AS week_name "
			+ "FROM perdidas "
			+ "WHERE create_at BETWEEN ?1 AND ?2 "
			+ "GROUP BY week_name "
			+ "ORDER BY YEAR(create_at) ASC, "
			+ "WEEK(create_at) ASC;", nativeQuery=true)
	public List<Object[]> findPerdidasXSemana(LocalDate fechIni, LocalDate fechaFin);

}
