package com.eareiza.springAngular.model.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eareiza.springAngular.model.entity.Factura;

@Repository
public interface IFacturaRepository extends CrudRepository<Factura, Long> {
	
	@Query(value="select p.nombre as producto, "
						+ "sum(fi.cantidad) as cantiad, "
						+ "inv.preciocompra as precio, "
						+ "inv.inventario_id as inventario, "
						+ "fi.id as factura "
					+ "from facturas_items fi "
					+ "inner join productos p "
					+ "	on fi.producto_id = p.id "
					+ "inner join inventarios_items inv "
					+ "	on inv.id = (select iteminventario_id "
					+ "					from itemsfactura_itemsinventario "
					+ "					where itemfactura_id = fi.id limit 1) "
					+ "where fi.consignacion = 1 "
					+ "group by fi.producto_id", nativeQuery = true)
	public List<Object[]> findConsignaciones();	
}
