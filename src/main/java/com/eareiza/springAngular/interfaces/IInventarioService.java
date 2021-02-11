package com.eareiza.springAngular.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eareiza.springAngular.model.entity.Cliente;
import com.eareiza.springAngular.model.entity.Inventario;

public interface IInventarioService {
	public List<Inventario> findAll();
	
	public Page<Inventario> findAll(Pageable pagina);
	
	//public Page<Inventario> buscarXEstado(Pageable pagina);
	
	public Inventario save(Inventario inventario);
	
	public Inventario findById(Long id);
	
	public void delete(Long id);
}
