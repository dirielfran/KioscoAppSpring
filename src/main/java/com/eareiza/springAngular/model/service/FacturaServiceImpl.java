package com.eareiza.springAngular.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.eareiza.springAngular.DTO.FacturaDto;
import com.eareiza.springAngular.DTO.PerdidaDto;
import com.eareiza.springAngular.interfaces.*;
import com.eareiza.springAngular.model.entity.*;
import com.eareiza.springAngular.utileria.Utileria;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eareiza.springAngular.DTO.ConsignacionDto;
import com.eareiza.springAngular.model.repository.IComisionRepository;
import com.eareiza.springAngular.model.repository.IFacturaRepository;
import com.eareiza.springAngular.model.repository.IItemFacturaRepository;
import com.eareiza.springAngular.model.repository.IProductoRepository;

// TODO: Auto-generated Javadoc
/**
 * The Class FacturaServiceImpl.
 */
@Service
public class FacturaServiceImpl implements IFacturaService {

	/** The facturas repo. */
	@Autowired
	private IFacturaRepository facturasRepo;
	
	/** The item inv serv. */
	@Autowired
	private IItemInventarioService itemInvServ;
	
	/** The comision repo. */
	@Autowired
	private IComisionRepository comisionRepo;
	
	/** The item fact repo. */
	@Autowired 
	private IItemFacturaRepository itemFactRepo;
	
	/** The producto repo. */
	@Autowired
	private IProductoRepository productoRepo;

	@Autowired
	private IInventarioService inventarioService;

	@Autowired
	private IGastosService gastoService;

	@Autowired
	private IProductoService productoService;

	private static final Utileria util = new Utileria();

	@Autowired
	private ModelMapper modelMapper;

	private boolean consignacion;

	/**
	 * Find by id.
	 *
	 * @param idFactura the id factura
	 * @return the factura
	 */
	@Override
	@Transactional(readOnly = true)
	public Factura findById(Long idFactura) {
		return facturasRepo.findById(idFactura).orElse(null);
	}

		
	/**
	 * Save factura. Metodo para la creacion de una factura
	 *
	 * @param factura que se crea
	 * @return Factura Retorna la Objeto de tipo factura creado
	 */	
	@Override
	@Transactional
	public Factura saveFactura(Factura factura) {
		//Se asina comision pr producto en a la factura en caso de existir code comision
		if( factura.getTipopago() != null ) asignacionComision(factura);
		for (ItemFactura item : factura.getItems()) {
			this.consignacion = false;
			Comision comision = comisionRepo.findbyProducto(item.getProducto().getId());
			//Se asigna comision por metodo de pago
			if (comision != null) item.setComision(comision.getComision());
			//Se recuperan los inventarios activos
			List<ItemInventario> items = itemInvServ.getInventarios(item.getProducto().getId(), "Activo");
			Double cantidad = BigDecimal.valueOf(item.getCantidad()).setScale(3, RoundingMode.HALF_UP).doubleValue();
			List<ItemInventario> inventAfect = new ArrayList<>();
			//Se valida existencia
			validaExistInv(items,cantidad, item,inventAfect, factura.isCosto());
			item.setConsignacion(consignacion);
			item.setItems_inventario(inventAfect);
			Producto producto = item.getProducto();
			//calculo de existencia
			BigDecimal numeroBg = BigDecimal.valueOf(producto.getExistencia()-item.getCantidad()).setScale(3, RoundingMode.HALF_UP);
			Double existencia = numeroBg.doubleValue();
			producto.setExistencia(existencia);
			productoService.saveProducto(producto);
		}
		factura.setUser(util.getUsuarioAuth());
		return facturasRepo.save(factura);
	}


	/**
	 * Metodo que valida existencia del producto, modifica la existencia del producto en el
	 * inventario y agrega el total a cada iten de factura
	 *
	 * @param inventarios Lista de inventarios activos del producto
	 * @param cantidad, cantidad que se requiere que exista
	 * @param item, Item de factura
	 * @param inventAfect, Inventarios a los que se les descuenta productos
	 */
	private void validaExistInv(List<ItemInventario> inventarios, Double cantidad,
								   ItemFactura item, List<ItemInventario> inventAfect, Boolean costo){
		Double total = 0D;
		for (ItemInventario itemInv : inventarios) {
			if(itemInv.getConsignacion()) this.consignacion = true;
			if (cantidad > 0) {
				Double existencia =  BigDecimal.valueOf(itemInv.getExistencia())
						.setScale(3, RoundingMode.HALF_UP).doubleValue();
				//Se valida si la existencia es menor o igual
				if(cantidad <= existencia) {
					itemInv.setExistencia(BigDecimal.valueOf(existencia-cantidad)
							.setScale(3, RoundingMode.HALF_UP).doubleValue());
					total += costo ? cantidad * itemInv.getPreciocompra() : cantidad * item.getPrecio();
					cantidad = 0D;
				}else {
					itemInv.setExistencia(0D);
					cantidad -= existencia;
					total += costo ? existencia * itemInv.getPreciocompra() : existencia * item.getPrecio();
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
		item.setTotal(total);
	}
	
	
	/**
	 * Asignacion comision. Metodo que asigna comision a una factura
	 *
	 * @param factura Es la factura a que se le asigna la comision
	 */
	private void asignacionComision(Factura factura) {
		List<Comision> comisiones = (List<Comision>) comisionRepo.findAll();
		//TODO falta exception en caso de que factura no tenga codigo de comision
		Comision comision = comisiones.stream()
			.filter(comi -> factura.getTipopago().get("code").equals(comi.getCode()))
			.findFirst().get();
		factura.setComision(comision);
	}

	/**
	 * Delete factura.
	 *
	 * @param idFactura the id factura
	 */
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

	/**
	 * Modifico factura.
	 *
	 * @param factura the factura
	 * @return the factura
	 */
	@Override
	@Transactional
	public Factura modificoFactura(Factura factura) {
		factura.setUser(util.getUsuarioAuth());
		return facturasRepo.save(factura);
	}


	/**
	 * Find consignacion.
	 *
	 * @return the list
	 */
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
			consignacion.setProductoId(Long.parseLong(String.valueOf(objects[4])));
			consignaciones.add(consignacion);
		}
		return consignaciones;
	}


	/**
	 * Save item factura.
	 *
	 * @param item the item
	 */
	@Override
	public void saveItemFactura(ItemFactura item) {
		itemFactRepo.save(item);
	}


	/**
	 * Find items factura.
	 *
	 * @param idProducto the id producto
	 * @return the list
	 */
	@Override
	public List<ItemFactura> findItemsFactura(Long idProducto) {
		 Optional<Producto> opt = productoRepo.findById(idProducto);
		 Producto producto = null;
		 if( opt.isPresent() ) {
			 producto = opt.get();
		 }
		return itemFactRepo.findByProductoAndConsignacion(producto, true);
	}


	/**
	 * Find item factura.
	 *
	 * @param idItem the id item
	 * @return the item factura
	 */
	@Override
	public ItemFactura findItemFactura(Long idItem) {
		return itemFactRepo.findById(idItem).orElse(null);
	}

	public void pagarConsignacion( ConsignacionDto consignacion, boolean mercadoPago){
		List<ItemFactura> facturas = findItemsFactura(consignacion.getProductoId());
		Inventario inventario = inventarioService.findById(consignacion.getInventario());
		gastoService.crearGastoInventario(inventario, "Consignacion", consignacion, mercadoPago);
		facturas.forEach(itemFactura -> {
			itemFactura.setConsignacion(false);
			saveItemFactura(itemFactura);
		});
	}

	@Transactional(readOnly = true)
	public List<FacturaDto> findFacturasCosto(){
		return facturasRepo.findByCosto(true)
				.stream().map(this::modelToDTO)
				.collect(Collectors.toList());
	}

	private FacturaDto modelToDTO(Factura factura){
		return modelMapper.map(factura, FacturaDto.class);
	}

	private Perdida dtoToModel(PerdidaDto perdidaDto){
		return modelMapper.map(perdidaDto, Perdida.class);
	}
}
