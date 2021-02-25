package com.eareiza.springAngular.model.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eareiza.springAngular.model.entity.Gastos;
import com.eareiza.springAngular.model.entity.Inventario;

@Repository
public interface GastosRepository extends JpaRepository<Gastos, Long> {

	
	public List<Gastos> findByFechaFact(Date fecha);

	public List<Gastos> findByUsuario(String usuario);
	
	public List<Gastos> findByClasificacion(String clasificacion);
	
	@Query("select g from Gastos g where inventario.id = ?1")
	public Gastos findByInventario(Long inventario);
	
	public Page<Gastos> findByOrderByIdDesc(Pageable page);
	
}
