package com.eareiza.springAngular.model.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.eareiza.springAngular.model.entity.ItemFactura;
import com.eareiza.springAngular.model.entity.Producto;

public interface IItemFacturaRepository extends CrudRepository<ItemFactura, Long> {
	
	List<ItemFactura> findByProductoAndConsignacion(Producto producto, Boolean consignacion);

}
