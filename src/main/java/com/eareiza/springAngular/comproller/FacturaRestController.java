
package com.eareiza.springAngular.comproller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eareiza.springAngular.interfaces.IFacturaService;
import com.eareiza.springAngular.interfaces.IProductoService;
import com.eareiza.springAngular.model.entity.Factura;
import com.eareiza.springAngular.model.entity.ItemFactura;
import com.eareiza.springAngular.model.entity.Producto;

// TODO: Auto-generated Javadoc
/**
 * The Class FacturaRestController.
 */
@CrossOrigin(origins = {"http://localhost:4200","*"})
@RestController
@RequestMapping("/api")
public class FacturaRestController {

	/** The fact serv. */
	@Autowired
	private IFacturaService factServ;
	
	/** The producto serv. */
	@Autowired
	private IProductoService productoServ;
	
	/**
	 * Show.
	 *
	 * @param idFactura the id factura
	 * @return the factura
	 */
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/facturas/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Factura show(@PathVariable("id") Long idFactura) {
		return factServ.findById(idFactura);
	}
	
	/**
	 * Delete.
	 *
	 * @param idFactura the id factura
	 */
	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("/facturas/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long idFactura) {
		Factura factura = factServ.findById(idFactura); 
		
		for (ItemFactura item : factura.getItems()) {
			Producto producto = item.getProducto();
			producto.setExistencia(producto.getExistencia()+item.getCantidad());
			productoServ.saveProducto(producto);
		}
		factServ.deleteFactura(idFactura);
	}
	
	/**
	 * Crear factura.
	 *
	 * @param factura the factura
	 * @return the factura
	 */
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/facturas")
	@ResponseStatus(HttpStatus.CREATED)
	public Factura crearFactura(@RequestBody Factura factura) {
		//Recorro items y descuento cantidad en producto
		for (ItemFactura item : factura.getItems()) {			
			Producto producto = item.getProducto();
			//calculo de existencia
			BigDecimal numeroBg = BigDecimal.valueOf(producto.getExistencia()-item.getCantidad()).setScale(3, RoundingMode.HALF_UP);
			Double existencia = numeroBg.doubleValue();
			producto.setExistencia(existencia);
			productoServ.saveProducto(producto);
		}
		return factServ.saveFactura(factura);
	}	
}
