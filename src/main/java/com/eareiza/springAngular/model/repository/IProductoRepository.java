package com.eareiza.springAngular.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eareiza.springAngular.model.entity.Producto;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {
	
	//QueriMthod con consulta personalizada
	@Query("SELECT p FROM Producto p WHERE p.nombre like %?1%")
	public List<Producto> findByNombre(String nombre);
	
	//Con QueryMethod, busca donde nombre contenga el parametro y se ignore si es may. o min.
	public List<Producto> findByNombreContainingIgnoreCase(String nombre);

	
	//Con QueryMethod, busca donde nombre comience con el parametro y se ignore si es may. o min.
	public List<Producto> findByNombreStartingWithIgnoreCase(String nombre);
}
