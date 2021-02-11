package com.eareiza.springAngular.interfaces;

import java.util.List;

import com.eareiza.springAngular.DTO.ConsignacionDto;
import com.eareiza.springAngular.model.entity.Factura;
import com.eareiza.springAngular.model.entity.ItemFactura;

public interface IFacturaService {
	
	Factura findById(Long idFactura);
	
	Factura saveFactura(Factura factura);
	
	Factura modificoFactura(Factura factura);
	
	void deleteFactura(Long idFactura);
	
	public List<ConsignacionDto> findConsignacion();
	
	void saveItemFactura(ItemFactura item);
	
	List<ItemFactura> findItemsFactura(Long idProducto);
	
	ItemFactura findItemFactura(Long idItem);

}
