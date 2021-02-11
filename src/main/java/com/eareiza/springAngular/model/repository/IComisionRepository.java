package com.eareiza.springAngular.model.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.eareiza.springAngular.model.entity.Comision;
import com.eareiza.springAngular.model.entity.Producto;

public interface IComisionRepository extends CrudRepository<Comision, Long> {
	
	@Query("from Comision c where c.producto.id = ?1")
	Comision findbyProducto(Long idProducto);
}
