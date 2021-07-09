package com.eareiza.springAngular.model.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.eareiza.springAngular.interfaces.IGastosService;
import com.eareiza.springAngular.model.entity.Gastos;
import com.eareiza.springAngular.model.repository.IItemFacturaRepository;
import com.eareiza.springAngular.utileria.Utileria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eareiza.springAngular.interfaces.IInventarioService;
import com.eareiza.springAngular.model.entity.Inventario;
import com.eareiza.springAngular.model.entity.ItemInventario;
import com.eareiza.springAngular.model.entity.Producto;
import com.eareiza.springAngular.model.repository.IInventarioRepository;
import com.eareiza.springAngular.model.repository.IProductoRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventarioServiceImpl implements IInventarioService {
	
	@Autowired
	IInventarioRepository inventarioRepo;
	
	@Autowired
	IProductoRepository productoRepo;

	@Autowired
	IItemFacturaRepository itemInventarioRepo;

	@Autowired
	IGastosService gastoServices;

	@Autowired
	private MessageSource mensajes;

	private final Utileria util = new Utileria();

	@Transactional(readOnly = true)
	@Override
	public List<Inventario> findAll() {
		return inventarioRepo.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Inventario> findAll(Pageable pagina) {
		return inventarioRepo.findByOrderByIdDesc(pagina);
	}

	@Transactional
	@Override
	public Inventario save(Inventario inventario) {
		inventario.setUser(util.getUsuarioAuth());
		return inventarioRepo.save(inventario);
	}

	/**
	 * Metodo que retorna un inventario por Id
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public Inventario findById(Long id) {
		return inventarioRepo.findById(id).orElseThrow(() ->
				new NoSuchElementException( mensajes.getMessage("error.inventario.notFoundException",
						null, null)));
	}

	/**
	 * Metodo que elimina inventario si no tiene Facturas asociadas
	 * @param idInventario
	 */
	@Transactional
	@Override
	public void delete(Long idInventario) {
		Optional<Inventario> opt = inventarioRepo.findById(idInventario);
		Inventario inventario = opt.orElseThrow(() ->
				new NoSuchElementException( mensajes.getMessage("error.inventario.notFoundException",
						null, null)));
		if ( inventario != null ) {
			for (ItemInventario itemInv : inventario.getItems()) {
				Producto producto = itemInv.getProducto();
				producto.setExistencia(producto.getExistencia()-itemInv.getStockadd());
				productoRepo.save(producto);
			}
			Gastos gasto = gastoServices.buscarPorInventario(idInventario);
			if( gasto != null ) gastoServices.borrarGasto( gasto.getId() );
		}
		inventarioRepo.deleteById(idInventario);
	}
}
