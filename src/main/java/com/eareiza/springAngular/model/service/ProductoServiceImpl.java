package com.eareiza.springAngular.model.service;

import java.util.List;

import com.eareiza.springAngular.utileria.Utileria;
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

	private final Utileria util = new Utileria();

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findProductoByNombre(String nombre) {
		return productoRepo.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(nombre, nombre);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto getbyId(Long idProducto) {
		return productoRepo.findById(idProducto).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> getProductos() {
		return productoRepo.findAll();
	}

	@Override
	@Transactional
	public Producto saveProducto(Producto producto) {
		producto.setUser(util.getUsuarioAuth());
		return productoRepo.save(producto);
	}

	@Override
	@Transactional
	public void deleteProducto(Long idProducto) {
		productoRepo.deleteById(idProducto);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Producto> findAll(Pageable pagina) {
		return productoRepo.findAll(pagina);
	}
}
