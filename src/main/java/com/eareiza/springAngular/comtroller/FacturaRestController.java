
package com.eareiza.springAngular.comtroller;

import java.util.List;

import com.eareiza.springAngular.DTO.FacturaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@CrossOrigin(origins = {"http://localhost:4200","*"})
@RestController
@RequestMapping("/api")
public class FacturaRestController {

	@Autowired
	private IFacturaService factServ;
	
	@Autowired
	private IProductoService productoServ;
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/facturas/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Factura show(@PathVariable("id") Long idFactura) {
		return factServ.findById(idFactura);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
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
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/facturas")
	@ResponseStatus(HttpStatus.CREATED)
	public Factura crearFactura(@RequestBody Factura factura) {
		return factServ.saveFactura(factura);
	}

	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/facturas/costo")
	public ResponseEntity<List<FacturaDto>> findFacturasCosto(){
		List<FacturaDto> facturas = factServ.findFacturasCosto();
		return new ResponseEntity<List<FacturaDto>>(factServ.findFacturasCosto(), HttpStatus.OK);
	}
}
