package com.eareiza.springAngular.model.service;


import java.time.LocalDate;
import java.util.List;

import com.eareiza.springAngular.DTO.CajaDto;
import com.eareiza.springAngular.exceptions.AperturaCajaException;
import com.eareiza.springAngular.utileria.PaginationComponent;
import com.eareiza.springAngular.utileria.Utileria;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

@Service
public class CajaServiceImpl implements ICajaService {

	@Autowired
	private ICajaRepository cajaRepo;
	
	@Autowired
	private ICajachicaService cajaService;

	private final Utileria util= new Utileria();

	@Autowired
	private PaginationComponent paginationComponent;

	@Autowired
	private ModelMapper modelMapper;

	public CajaServiceImpl() {
	}

	@Override
	@Transactional(readOnly = true)
	public List<Caja> findAll() {
		return cajaRepo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CajaDto> finAll(Integer page) throws NotFoundException {
		Pageable pagina = PageRequest.of(page, 10);
		Page<Caja> cajas = cajaRepo.findByOrderByIdDesc(pagina);
		if(cajas.getTotalPages() <= pagina.getPageNumber()){
			throw new NotFoundException("No existe la pagina: "+page);
		}
		return cajas.map(this::modelToDTO);
	}


	private CajaDto modelToDTO(Caja caja){
		return modelMapper.map(caja, CajaDto.class);
	}

	private Caja dtoToModel(CajaDto cajaDto){
		return modelMapper.map(cajaDto, Caja.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Caja findById(Long idCaja) {
		return cajaRepo.findById(idCaja).orElse(null);
	}

	/**
	 * Metodo saveCaja.  Registra la caja y los movimientos de saldo de cada una de las 
	 * facturas que pertenecen a la caja
	 *
	 * @param caja Objeto de tipo Caja representa los movimientos de saldo 
	 * @return caja. Objeto de tipo Caja creado
	 */
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
			cajaService.registroCaja(factura);
		}
		if( caja.getFacturas().isEmpty() && validaAperturaCaja()) throw new AperturaCajaException();
		caja.setGanancia(ganancias);
		caja.setUser(util.getUsuarioAuth());
		return cajaRepo.save(caja);
	}

	private boolean validaAperturaCaja(){
		return cajaRepo.countByEstado("Abierto") > 0;
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

	/*
	 * Metodo que obtiene las diferencias del mes actual
	 * */
	@Override
	public Double findDiferenciasXMes() {
		LocalDate fecha = LocalDate.now();
		LocalDate desde = fecha.withDayOfMonth(1);
		LocalDate hasta = fecha.withDayOfMonth(fecha.lengthOfMonth());
		Object obj = cajaRepo.findDiferenciasXMes(desde, hasta);
		Double perdidas = (Double) obj;
		return perdidas;
	}

}
