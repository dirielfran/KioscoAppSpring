package com.eareiza.springAngular.comtroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eareiza.springAngular.DTO.ConsignacionDto;
import com.eareiza.springAngular.interfaces.IFacturaService;
import com.eareiza.springAngular.interfaces.IGastosService;
import com.eareiza.springAngular.interfaces.IInventarioService;
import com.eareiza.springAngular.model.entity.Inventario;
import com.eareiza.springAngular.model.entity.ItemFactura;

@RestController
@RequestMapping("/api")
//Se configuran los dominios permitidos, soporta una lista d dominios
//Se pueden especificar los metodos permitidos, las cabeceras
@CrossOrigin(origins= {"http://localhost:4200","*"})
public class ConsignacionRestController {
	
	@Autowired
	private IFacturaService facturasService;
	
	@Autowired
	private IInventarioService inventarioService;
	
	@Autowired
	private IGastosService gastoService;
	
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/consignaciones")
	public ResponseEntity<?> consignaciones(){
		List<ConsignacionDto> consignaciones= null;
		//Se crea Map para el envio de mensaje de error en el ResponseEntity
		Map<String, Object> response = new HashMap<>();
		try {
			consignaciones = facturasService.findConsignacion();
		}catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//Se valida si el inventario es null y se maneja el error
		if(consignaciones.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<List<ConsignacionDto>>(consignaciones, HttpStatus.OK);
	}
	
	//Se le añade seguridad a los endpoint por url
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/consignaciones")
	public ResponseEntity<?> crearGasto(@Valid @RequestBody ConsignacionDto consignacion, BindingResult result, @RequestParam("mercadoPago") Boolean mercadoPago){
		//Se agrega map para el envio de mensaje y obj en el response
		Map<String, Object> response = new HashMap<>();
		
		//Validacion de errores del binding result
		if(result.hasErrors()) {
			List<String> errores = result.getFieldErrors()
					//Se convierten a stream
					.stream()
					//Cada error se convierte en un tipo String
					.map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage())
					//Se convierte en una lista 
					.collect(Collectors.toList());
			response.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		//Manejo de errores utilizando el obj de spring DataAccessException
		try {
			ItemFactura itemFactura = facturasService.findItemFactura(consignacion.getFactura());		
			List<ItemFactura> facturas = facturasService.findItemsFactura(itemFactura.getProducto().getId());
			Inventario inventario = inventarioService.findById(consignacion.getInventario());
			gastoService.crearGastoInventario(inventario, "Consignacion", consignacion, mercadoPago);
			for (ItemFactura factura : facturas) {
				factura.setConsignacion(false);
				facturasService.saveItemFactura(factura);
			}
		} catch (DataAccessException e) {
			//Se crean mensajes de error y se añaden all map
			response.put("mensaje", "Error al crear gasto en la Base de Datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			//Se crea respuesta enviando los mensajes del error y el estatus
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//Se agrega mensaje success al mapa
		response.put("mensaje", "El gasto se ha registrado");	
		//Se retorna respuesta añadiendo mapa y estatus
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}	
	
}
