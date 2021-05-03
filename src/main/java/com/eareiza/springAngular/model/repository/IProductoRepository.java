package com.eareiza.springAngular.model.repository;

import java.time.LocalDate;
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
	
	@Query(value="SELECT  p.nombre AS nombre, SUM(fi.cantidad) AS cantidad, "
					+ "SUM(((fi.precio-ii.preciocompra)*fi.cantidad)+fi.comision) AS ganancia, "
					+ "SUM(((fi.precio)*fi.cantidad)) AS venta "
				+ "FROM facturas_items fi "
				+ "INNER JOIN facturas f ON f.id = fi.factura_id "
				+ "INNER JOIN productos p ON p.id = fi.producto_id "
				+ "INNER JOIN itemsfactura_itemsinventario piv ON fi.id = piv.itemfactura_id "
				+ "INNER JOIN inventarios_items ii ON ii.id = piv.iteminventario_id "
				+ "WHERE f.create_at BETWEEN ?1 AND ?2 "
				+ "GROUP BY p.id "
				+ "ORDER BY 3 DESC "
				+ "LIMIT 10", nativeQuery=true)
	public List<Object[]> findProductosTop(LocalDate fechIni, LocalDate fechaFin);
	
	@Query(value="SELECT  p.id "
			+ "FROM facturas_items fi "
			+ "INNER JOIN facturas f ON f.id = fi.factura_id "
			+ "INNER JOIN productos p ON p.id = fi.producto_id "
			+ "INNER JOIN itemsfactura_itemsinventario piv ON fi.id = piv.itemfactura_id "
			+ "INNER JOIN inventarios_items ii ON ii.id = piv.iteminventario_id "
			+ "WHERE f.create_at BETWEEN ?1 AND ?2 "
			+ "GROUP BY p.id "
			+ "ORDER BY SUM(((fi.precio-ii.preciocompra)*fi.cantidad)+fi.comision) DESC "
			+ "LIMIT 10", nativeQuery=true)
	public List<Integer> findTopId(LocalDate fechIni, LocalDate fechaFin);
	
	@Query(value="SELECT  p.nombre AS nombre, "
			+ "	SUM(((fi.precio-ii.preciocompra)*fi.cantidad)+fi.comision) AS ganancia "
			+ "FROM facturas_items fi  "
			+ "INNER JOIN facturas f ON f.id = fi.factura_id "
			+ "INNER JOIN productos p ON p.id = fi.producto_id  "
			+ "INNER JOIN itemsfactura_itemsinventario piv ON fi.id = piv.itemfactura_id  "
			+ "INNER JOIN inventarios_items ii ON ii.id = piv.iteminventario_id  "
			+ "WHERE f.create_at BETWEEN ?1 AND ?2 "
			+ "and p.id in ?3 "
			+ "GROUP BY p.id, MONTH(f.create_at) "
			+ "ORDER BY p.id, MONTH(f.create_at), 2 ASC ", nativeQuery = true)
	public List<Object[]> findT0pX3(LocalDate fechIni, LocalDate fechaFin, List<Integer> ids);
	
	@Query(value="SELECT SUM((ii.existencia*ii.preciocompra)/?1) "
			+ "FROM ItemInventario ii "
			+ "INNER JOIN Producto p on p.id = ii.producto "
			+ "WHERE ii.estado = 'Activo' ")
	public Double findPatrimonio(Double dolar);	
	
	
}
