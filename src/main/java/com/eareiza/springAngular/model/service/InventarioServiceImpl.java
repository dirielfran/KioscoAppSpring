package com.eareiza.springAngular.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eareiza.springAngular.interfaces.IInventarioService;
import com.eareiza.springAngular.model.entity.Inventario;
import com.eareiza.springAngular.model.entity.ItemInventario;
import com.eareiza.springAngular.model.entity.Producto;
import com.eareiza.springAngular.model.repository.IInventarioRepository;
import com.eareiza.springAngular.model.repository.IProductoRepository;

@Service
public class InventarioServiceImpl implements IInventarioService {
	
	@Autowired
	IInventarioRepository inventarioRepo;
	
	@Autowired
	IProductoRepository productoRepo;

	@Override
	public List<Inventario> findAll() {
		return inventarioRepo.findAll();
	}

	@Override
	public Page<Inventario> findAll(Pageable pagina) {
		Page<Inventario> inventario = inventarioRepo.findAll(pagina);
		return inventario;
	}

	@Override
	public Inventario save(Inventario inventario) {
		return inventarioRepo.save(inventario);
	}

	@Override
	public Inventario findById(Long id) {
		return inventarioRepo.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		Optional<Inventario> opt = inventarioRepo.findById(id);
		Inventario inventario = opt.isPresent() ? opt.get() : null;
		if ( inventario != null ) {
			for (ItemInventario itemInv : inventario.getItems()) {
				Producto producto = itemInv.getProducto();
				producto.setExistencia(producto.getExistencia()-itemInv.getStockadd());
				productoRepo.save(producto);
			}			
		}
		inventarioRepo.deleteById(id);
	}

}
