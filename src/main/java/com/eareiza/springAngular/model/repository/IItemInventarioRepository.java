package com.eareiza.springAngular.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.eareiza.springAngular.model.entity.ItemInventario;

public interface IItemInventarioRepository extends CrudRepository<ItemInventario, Long> {
	@Query(value="Select * from inventarios_items i where i.producto_id = ?1 and i.estado= ?2", nativeQuery = true)
	List<ItemInventario> findItemInventario(Long idProducto, String estado);
}
