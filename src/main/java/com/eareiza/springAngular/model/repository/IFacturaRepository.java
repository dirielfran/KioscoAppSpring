package com.eareiza.springAngular.model.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eareiza.springAngular.model.entity.Factura;

@Repository
public interface IFacturaRepository extends CrudRepository<Factura, Long> {
	
	@Query(value="select p.nombre as producto, " +
			"		sum(fi.cantidad) as cantiad, " +
			"		(select preciocompra " +
			"			from inventarios_items " +
			"			where producto_id = p.id limit 1) as precio, " +
			"		(select inventario_id " +
			"			from inventarios_items " +
			"			where producto_id = p.id limit 1) as inventario," +
			"		p.id as idProducto " +
			"from facturas_items fi " +
			"inner join productos p " +
			"	on fi.producto_id = p.id " +
			"where fi.consignacion = 1 " +
			"group by fi.producto_id", nativeQuery = true)
	public List<Object[]> findConsignaciones();	
	
	@Query(value="select sum(total)/1000, DAY(create_at) "
			+ "		from facturas "
			+ "		WHERE MONTH(create_at) = ?1 "
			+ "		AND YEAR(create_at) = ?2 "
			+ "		group by DAY(create_at)", nativeQuery = true)
	public List<Object[]> findVentasXUlt30(Integer mes, Integer anio);
	
	@Query(value="SELECT SUM(((fi.precio-ii.preciocompra)*fi.cantidad)+fi.comision) AS ganancia, DAYOFMONTH(f.create_at) "
			+ "FROM facturas_items fi "
			+ "INNER JOIN facturas f ON f.id = fi.factura_id "
			+ "INNER JOIN itemsfactura_itemsinventario piv ON fi.id = piv.itemfactura_id "
			+ "INNER JOIN inventarios_items ii ON ii.id = piv.iteminventario_id "
			+ "WHERE f.create_at BETWEEN ?1 AND ?2 "
			+ "GROUP BY DAY(f.create_at)", nativeQuery = true)
	public List<Object[]> findGananciasXUlt7(LocalDate fechIni, LocalDate fechaFin);
	
	@Query(value="SELECT  SUM(f.total) "
			+ "FROM Factura f "
			+ "WHERE YEAR(f.createAt) = ?1 "
			+ "GROUP BY MONTH(f.createAt) "
			+ "ORDER BY MONTH(f.createAt) ASC")
	public List<Double> findVentasXMes(Integer anio);
	
	@Query(value="SELECT SUM(((fi.precio-ii.preciocompra)*fi.cantidad)+fi.comision)  "
			+ "from facturas f "
			+ "LEFT JOIN facturas_items fi ON f.id = fi.factura_id "
			+ "INNER JOIN itemsfactura_itemsinventario piv ON fi.id = piv.itemfactura_id "
			+ "INNER JOIN inventarios_items ii ON ii.id = piv.iteminventario_id "
			+ "WHERE YEAR(f.create_at) = 2021 "
			+ "GROUP BY MONTH(f.create_at) "
			+ "ORDER BY MONTH(f.create_at) ASC", nativeQuery = true)
	public List<Double> findGananciasXMes();

	public List<Factura> findByCosto(boolean costo);

}
