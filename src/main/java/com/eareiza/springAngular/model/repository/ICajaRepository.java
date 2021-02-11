package com.eareiza.springAngular.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eareiza.springAngular.model.entity.Caja;

public interface ICajaRepository extends JpaRepository<Caja, Long> {
	
	Long countByEstado(String estado);
	Caja findByEstado(String estado);
	

}