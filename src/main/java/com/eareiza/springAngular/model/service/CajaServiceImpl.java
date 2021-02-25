package com.eareiza.springAngular.model.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eareiza.springAngular.interfaces.ICajaService;
import com.eareiza.springAngular.interfaces.ICajachicaService;
import com.eareiza.springAngular.model.entity.Caja;
import com.eareiza.springAngular.model.entity.Factura;
import com.eareiza.springAngular.model.entity.ItemFactura;
import com.eareiza.springAngular.model.entity.ItemInventario;
import com.eareiza.springAngular.model.repository.ICajaRepository;
import com.eareiza.springAngular.model.repository.IInventarioRepository;

@Service
public class CajaServiceImpl implements ICajaService {

	@Autowired
	private ICajaRepository cajaRepo;
	
	@Autowired
	private IInventarioRepository inventarioRepo;
	
	@Autowired
	private ICajachicaService cajaService;
	
	@Override
	@Transactional(readOnly = true)
	public List<Caja> findAll() {
		return cajaRepo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Caja> finAll(Pageable pagina) {
		return cajaRepo.findByOrderByIdDesc(pagina);
	}

	@Override
	@Transactional(readOnly = true)
	public Caja findById(Long idCaja) {
		return cajaRepo.findById(idCaja).orElse(null);
	}

	@Override
	@Transactional()
	public Caja saveCaja(Caja caja) {
		Double ganancias = 0D;
		for (Factura factura : caja.getFacturas()) {
			for (ItemFactura item : factura.getItems()) {
				Double cantidad = item.getCantidad();
				Double precio = item.getPrecio();
				for (ItemInventario inventario : item.getItems_inventario()) {
					if(item.getItem_inventario() != null && item.getItem_inventario().getId() == inventario.getId()) {
						cantidad -= item.getCantinv();
						ganancias += item.getCantinv() * (precio - inventario.getPreciocompra());
					}else {
						ganancias += cantidad * (precio - inventario.getPreciocompra());
					}					
				}				
				ganancias += item.getComision();
			}
			//Se registra factura en la cajachica
			cajaService.registroCaja(factura);
		}
		caja.setGanancia(ganancias);
		return cajaRepo.save(caja);
	}

	@Override
	@Transactional()
	public void deleteCaja(Long idCaja) {
		cajaRepo.deleteById(idCaja);
	}

	@Override
	public Long countByEstadoCaja(String estado) {
		return cajaRepo.countByEstado(estado);
	}

	@Override
	public Caja buscarXEstado(String estado) {
		return cajaRepo.findByEstado(estado);
	}

}
