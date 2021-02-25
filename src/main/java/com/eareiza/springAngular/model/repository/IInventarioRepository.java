package com.eareiza.springAngular.model.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eareiza.springAngular.model.entity.Inventario;

public interface IInventarioRepository extends JpaRepository<Inventario, Long> {

	public Page<Inventario> findByOrderByIdDesc(Pageable page);
	
}
