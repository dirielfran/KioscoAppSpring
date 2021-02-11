package com.eareiza.springAngular.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eareiza.springAngular.DTO.ConsignacionDto;
import com.eareiza.springAngular.interfaces.ICajachicaService;
import com.eareiza.springAngular.interfaces.IFacturaService;
import com.eareiza.springAngular.interfaces.IGastosService;
import com.eareiza.springAngular.interfaces.IItemInventarioService;
import com.eareiza.springAngular.model.entity.Cajachica;
import com.eareiza.springAngular.model.entity.Comision;
import com.eareiza.springAngular.model.entity.Factura;
import com.eareiza.springAngular.model.entity.ItemFactura;
import com.eareiza.springAngular.model.entity.ItemInventario;
import com.eareiza.springAngular.model.entity.Producto;
import com.eareiza.springAngular.model.repository.IComisionRepository;
import com.eareiza.springAngular.model.repository.IFacturaRepository;
import com.eareiza.springAngular.model.repository.IItemFacturaRepository;
import com.eareiza.springAngular.model.repository.IProductoRepository;

@Service
public class FacturaServiceImpl implements IFacturaService {

	@Autowired
	private IFacturaRepository facturasRepo;
	
	@Autowired
	private IItemInventarioService itemInvServ;
	
	@Autowired
	private IComisionRepository comisionRepo;
	
	@Autowired
	private ICajachicaService cajaService;
	
	@Autowired 
	private IItemFacturaRepository itemFactRepo;
	
	@Autowired
	private IProductoRepository productoRepo;
	
	@Override
	@Transactional(readOnly = true)
	public Factura findById(Long idFactura) {
		return facturasRepo.findById(idFactura).orElse(null);
	}

		
	//Metodo para la creacion de una factura
	@Override
	@Transactional
	public Factura saveFactura(Factura factura) {
		//Se recorren los items de la factura
		for (ItemFactura item : factura.getItems()) {
			boolean consignacion = false;
			Comision comision = comisionRepo.findbyProducto(item.getProducto().getId());
			if (comision != null) item.setComision(comision.getComision());
			//Se recuperan los inventarios activos
			List<ItemInventario> items = itemInvServ.getInventarios(item.getProducto().getId(), "Activo");
			Double cantidad = BigDecimal.valueOf(item.getCantidad()).setScale(3, RoundingMode.HALF_UP).doubleValue();
			List<ItemInventario> inventAfect = new ArrayList<>();
			//Se recorren los inventarios activos
			for (ItemInventario itemInv : items) {
				if(itemInv.getConsignacion()==true) consignacion = true;
				if (cantidad > 0) {
					Double existencia =  BigDecimal.valueOf(itemInv.getExistencia()).setScale(3, RoundingMode.HALF_UP).doubleValue();
					//Se valida si la existencia es menor o igual
					if(cantidad <= existencia) {
						//se disminuye la existencia
						itemInv.setExistencia(BigDecimal.valueOf(existencia-cantidad).setScale(3, RoundingMode.HALF_UP).doubleValue());
						cantidad = 0D;
					}else {
						//Se setea a cero la existencia del inventario
						itemInv.setExistencia(0D);
						//Se disminuye la cantidad para que pueda ser descontada en el siguiente inventario
						cantidad -= existencia;
					}
					if(itemInv.getExistencia() == 0) {
						itemInv.setEstado("Inactivo");	
						item.setItem_inventario(itemInv);
						item.setCantinv(existencia);
					}
					itemInvServ.saveItemInventario(itemInv);	
					inventAfect.add(itemInv);
				}
			}
			item.setConsignacion(consignacion);
			item.setItems_inventario(inventAfect);
		}
		//Se registra factura en la cajachica
		//cajaService.registroCaja(factura);
		return facturasRepo.save(factura);
	}
	
	

	//Metodo para borrar una factura
	@Override
	@Transactional
	public void deleteFactura(Long idFactura) {	
		//Recupero la factura
		Optional<Factura> opt = facturasRepo.findById(idFactura);
		Factura factura = null;
		if(opt.isPresent()) {
			factura = opt.get();
		}
		//Valido que hay factura
		if (factura != null) {
			//Recorro los item de la factura
			for (ItemFactura item : factura.getItems()) {
				//Se obtiene la cantidad a reponer
				Double cantidad = item.getCantidad();
				Long idInvInactivo = 0L;
				//Se valida si el item inactivo un inventario
				if(item.getItem_inventario() != null) {					
					//Activo el inventario y repongo la existencia
					ItemInventario invInactivo = itemInvServ.getItemInventario(item.getItem_inventario().getId());
					idInvInactivo = invInactivo.getId();
					invInactivo.setEstado("Activo");
					invInactivo.setExistencia(item.getCantinv());
					cantidad -= item.getCantinv();
					itemInvServ.saveItemInventario(invInactivo);
				}
				//Recorro los inventarios activos
				for (ItemInventario itemInv : item.getItems_inventario()) {		
					if (idInvInactivo != itemInv.getId()) {
						//se aumenta la existencia
						itemInv.setExistencia(itemInv.getExistencia()+cantidad);
						itemInv.setEstado("Activo");
						cantidad -= cantidad;
						itemInvServ.saveItemInventario(itemInv);
					}										
				}				
			}
			//Se elimina registro de factura en caja
//			cajaService.deleteRegistroCaja(factura);
//			Cajachica caja = cajaService.findByFactura(idFactura);
//			cajaService.deleteCaja(caja.getId());			
		}
		facturasRepo.deleteById(idFactura);
	}




	@Override
	@Transactional
	public Factura modificoFactura(Factura factura) {
		return facturasRepo.save(factura);
	}


	@Override
	@Transactional(readOnly = true)
	public List<ConsignacionDto> findConsignacion() {
		List<Object[]> prueba = facturasRepo.findConsignaciones() ;
		List<ConsignacionDto> consignaciones= new ArrayList<>();
		for (Object[] objects : prueba) {
			ConsignacionDto consignacion = new ConsignacionDto();
			consignacion.setProducto((String) objects[0]);
			consignacion.setCantidad((Double) objects[1]);
			consignacion.setPrecio((Double) objects[2]);
			consignacion.setInventario(Long.parseLong(String.valueOf(objects[3])));
			consignacion.setFactura(Long.parseLong(String.valueOf(objects[4])));
			consignaciones.add(consignacion);
		}
		return consignaciones;
	}


	@Override
	public void saveItemFactura(ItemFactura item) {
		itemFactRepo.save(item);
	}


	@Override
	public List<ItemFactura> findItemsFactura(Long idProducto) {
		 Optional<Producto> opt = productoRepo.findById(idProducto);
		 Producto producto = null;
		 if( opt.isPresent() ) {
			 producto = opt.get();
		 }
		return itemFactRepo.findByProductoAndConsignacion(producto, true);
	}


	@Override
	public ItemFactura findItemFactura(Long idItem) {
		return itemFactRepo.findById(idItem).orElse(null);
	}
}