package com.eareiza.springAngular.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eareiza.springAngular.model.entity.Producto;

public interface IProductoService {
	List<Producto> findProductoByNombre(String nombre);
	Producto getbyId(Long idProducto);
	List<Producto> getProductos();
	Producto saveProducto(Producto producto);
	void deleteProducto(Long idProducto);
	Page<Producto> findAll(Pageable pagina);	
}
