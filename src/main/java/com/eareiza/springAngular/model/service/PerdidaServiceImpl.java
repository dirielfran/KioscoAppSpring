package com.eareiza.springAngular.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.eareiza.springAngular.DTO.CajaDto;
import com.eareiza.springAngular.DTO.PerdidaDto;
import com.eareiza.springAngular.model.entity.Caja;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eareiza.springAngular.interfaces.IItemInventarioService;
import com.eareiza.springAngular.interfaces.IPerdidaService;
import com.eareiza.springAngular.interfaces.IProductoService;
import com.eareiza.springAngular.model.entity.ItemInventario;
import com.eareiza.springAngular.model.entity.Perdida;
import com.eareiza.springAngular.model.repository.IPerdidaRepository;
import com.eareiza.springAngular.model.repository.IProductoRepository;

@Service
public class PerdidaServiceImpl implements IPerdidaService {
	
	@Autowired
	private IPerdidaRepository perdidasRepo;
	
	@Autowired
	private IItemInventarioService itemInvServ;
	
	@Autowired
	private IProductoRepository productoRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	@Transactional(readOnly = true)
	public List<Perdida> findAll() {
		return perdidasRepo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<PerdidaDto> finAll(Integer page) throws NotFoundException {
		Pageable pagina = PageRequest.of(page, 5);
		Page<Perdida> perdidas = perdidasRepo.findByOrderByIdDesc(pagina);
		if(perdidas.getTotalPages() <= pagina.getPageNumber()){
			throw new NotFoundException("No existe la pagina: "+page);
		}
		Page<PerdidaDto> perdidasDto = perdidas.map(this::modelToDTO);
		return perdidas.map(this::modelToDTO);
	}

	private PerdidaDto modelToDTO(Perdida perdida){
		return modelMapper.map(perdida, PerdidaDto.class);
	}

	private Perdida dtoToModel(PerdidaDto perdidaDto){
		return modelMapper.map(perdidaDto, Perdida.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Perdida findById(Long idPerdida) {
		return perdidasRepo.findById(idPerdida).orElse(null);
	}

	@Override
	@Transactional
	public Perdida savePerdida(Perdida perdida) {
		//Se recuperan los inventarios activos
		List<ItemInventario> items = itemInvServ.getInventarios(perdida.getProducto().getId(), "Activo");
		Double cantidad = BigDecimal.valueOf(perdida.getCantidad()).setScale(3, RoundingMode.HALF_UP).doubleValue();
		List<ItemInventario> inventAfect = new ArrayList<>();
		Double existenciaProd = perdida.getProducto().getExistencia();
		perdida.getProducto().setExistencia(existenciaProd-cantidad);
		//Se recorren los inventarios activos
		for (ItemInventario itemInv : items) {
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
					perdida.setItem_inventario(itemInv);
					perdida.setCantinv(existencia);
				}
				itemInvServ.saveItemInventario(itemInv);	
				inventAfect.add(itemInv);
			}
		}
		productoRepo.save(perdida.getProducto());
		perdida.setItems_inventario(inventAfect);
		return perdidasRepo.save(perdida);
	}

	@Override
	@Transactional
	public void deletePerdida(Long idPerdida) {
		Optional<Perdida> opt = perdidasRepo.findById(idPerdida);
		Perdida perdida = null;
		if (opt.isPresent()) {
			perdida = opt.get();
			//Se obtiene la existencia del producto y se repone
			Double cantProd = perdida.getProducto().getExistencia();
			perdida.getProducto().setExistencia(cantProd+perdida.getCantidad());
			productoRepo.save(perdida.getProducto());
			//Se obtiene la cantidad a reponer
			Double cantidad = perdida.getCantidad();
			Long idInvInactivo = 0L;
			//Se valida si el item inactivo un inventario
			if(perdida.getItem_inventario() != null) {	
				//Activo el inventario y repongo la existencia
				ItemInventario invInactivo = itemInvServ.getItemInventario(perdida.getItem_inventario().getId());
				idInvInactivo = invInactivo.getId();
				invInactivo.setEstado("Activo");
				invInactivo.setExistencia(perdida.getCantinv());
				cantidad -= perdida.getCantinv();
				itemInvServ.saveItemInventario(invInactivo);
			}
			//Recorro los inventarios activos
			for (ItemInventario itemInv : perdida.getItems_inventario()) {		
				if (idInvInactivo != itemInv.getId()) {
					//se aumenta la existencia
					itemInv.setExistencia(itemInv.getExistencia()+cantidad);
					itemInv.setEstado("Activo");
					cantidad -= cantidad;
					itemInvServ.saveItemInventario(itemInv);
				}										
			}	
		}	
		
		perdidasRepo.deleteById(idPerdida);
	}

	/*
	 * Metodo que obtiene las perdidas del mes actual
	 * */
	@Override
	public Double findPerdidasXMes() {
		LocalDate fecha = LocalDate.now();
		LocalDate desde = fecha.withDayOfMonth(1);
		LocalDate hasta = fecha.withDayOfMonth(fecha.lengthOfMonth());
		Object obj = perdidasRepo.findPerdidasXMes(desde, hasta);
		Double perdidas = (Double) obj;
		return perdidas;
	}
}
