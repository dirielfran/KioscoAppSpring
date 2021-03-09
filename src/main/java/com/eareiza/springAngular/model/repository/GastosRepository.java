package com.eareiza.springAngular.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eareiza.springAngular.model.entity.Gastos;

@Repository
public interface GastosRepository extends JpaRepository<Gastos, Long> {

	
	public List<Gastos> findByFechaFact(Date fecha);

	public List<Gastos> findByUsuario(String usuario);
	
	public List<Gastos> findByClasificacion(String clasificacion);
	
	@Query(value="select SUM(g.monto_pesos) from Gastos g where g.clasificacion = ?1 and g.fecha_fact between ?2 and ?3", nativeQuery = true)
	public Object findGastosXMes(String gasto, LocalDate fechIni, LocalDate fechaFin);
	
	@Query("select g from Gastos g where inventario.id = ?1")
	public Gastos findByInventario(Long inventario);
	

	public Page<Gastos> findByOrderByIdDesc(Pageable page);

	@Query(value="select  sum(((fi.precio-ii.preciocompra)*fi.cantidad)+fi.comision) as ganancia "
			+ "	from facturas_items fi"
			+ "	inner join facturas f on f.id = fi.factura_id"
			+ "	inner join productos p on p.id = fi.producto_id"
			+ "	inner join itemsfactura_itemsinventario piv on fi.id = piv.itemfactura_id"
			+ "	inner join inventarios_items ii on ii.id = piv.iteminventario_id "
			+ " where f.create_at between ?1 and ?2", nativeQuery = true )
	public Object findGananciasXMes(LocalDate fechaIni, LocalDate fechaFin);
}
