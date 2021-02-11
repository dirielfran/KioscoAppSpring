package com.eareiza.springAngular.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eareiza.springAngular.interfaces.IProductoService;
import com.eareiza.springAngular.model.entity.Producto;
import com.eareiza.springAngular.model.repository.IProductoRepository;

@Service
public class ProductoServiceImpl implements IProductoService {
	
	@Autowired
	private IProductoRepository productoRepo;

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findProductoByNombre(String nombre) {
		return productoRepo.findByNombreContainingIgnoreCase(nombre);
	}

	@Override
	public Producto getbyId(Long idProducto) {
		return productoRepo.findById(idProducto).orElse(null);
	}

	@Override
	public List<Producto> getProductos() {
		return productoRepo.findAll();
	}

	@Override
	public Producto saveProducto(Producto producto) {
		return productoRepo.save(producto);
	}

	@Override
	public void deleteProducto(Long idProducto) {
		productoRepo.deleteById(idProducto);
	}

	@Override
	public Page<Producto> findAll(Pageable pagina) {
		return productoRepo.findAll(pagina);
	}
}
