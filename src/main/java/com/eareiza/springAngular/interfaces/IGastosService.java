package com.eareiza.springAngular.interfaces;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eareiza.springAngular.DTO.ConsignacionDto;
import com.eareiza.springAngular.model.entity.Gastos;
import com.eareiza.springAngular.model.entity.Inventario;

public interface IGastosService {	
	Gastos guardar(Gastos gasto);
	List<Gastos> buscarTodos();
	Gastos buscarPorId(Long id);
	List<Gastos> buscarPorFecha(Date fechaGasto);
	List<Gastos> buscarPorUsuario(String Usuario);
	void borrarGasto(Long id);
	List<Gastos> buscarPorClasificacion(String clasificacion);
	Page<Gastos> getGastos(Pageable pagina);
	Gastos buscarPorInventario(Long inventario);
	public void crearGastoInventario(Inventario inventario, String tipo, ConsignacionDto consignacion, Boolean mercadoPago);
	public Double findGanancias();
	public Double findGastosxmes();
}
