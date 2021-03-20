package com.eareiza.springAngular.comproller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eareiza.springAngular.interfaces.ICajaService;
import com.eareiza.springAngular.interfaces.ICajachicaService;
import com.eareiza.springAngular.interfaces.IFacturaService;
import com.eareiza.springAngular.interfaces.IRetiroCajaService;
import com.eareiza.springAngular.model.entity.Caja;
import com.eareiza.springAngular.model.entity.Cliente;
import com.eareiza.springAngular.model.entity.Factura;
import com.eareiza.springAngular.model.entity.RetiroCaja;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200","*"})
public class CajaController {

	@Autowired
	ICajaService cajaService;
	
	@Autowired
	IFacturaService facturaService;
	
	@Autowired
	IRetiroCajaService retiroService;
	
	@Autowired
	ICajachicaService cajachicaService;
	
	@GetMapping("/caja")
	public List<Caja> cajas(){
		return cajaService.findAll();
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/caja/page/{page}")
	public Page<Caja> cajaPage(@PathVariable Integer page){
		Pageable pagina = PageRequest.of(page, 5);
		return cajaService.finAll(pagina);
	}
	
	
	//Se recupera caja por id
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/caja/{id}")
	public ResponseEntity<?> showCaja(@PathVariable Long idCaja){
		Caja caja = null;
		Map<String, Object> response = new HashMap<>();
		try {
			//Se recupera la caja de la clase de servicio
			caja = cajaService.findById(idCaja);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			//Se envia error a la vista
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//Se valida si la caja esta en null
		if(caja == null) {
			response.put("mensaje", "La caja Id: ".concat(idCaja.toString())
					.concat(" no existe en la base de datos."));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Caja>(caja, HttpStatus.OK);
	}
	
	//Metodo para la creacion de caja
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/caja")
	public ResponseEntity<?> saveCaja(@Valid @RequestBody Caja caja, BindingResult result){
		Caja cajaNew = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			List<String> errores = result.getFieldErrors()
				//Se convierte en stream
				.stream()
				//Cada error se convierte en un string
				.map(error -> "El campo '"+error.getField()+"' "+error.getDefaultMessage())
				//Se convierte en una lista
				.collect(Collectors.toList());
			response.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		try {
			cajaNew = cajaService.saveCaja(caja);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar la Caja.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje","La caja se ha registrado.");
		response.put("caja", cajaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	//Metodo para modificar Caja
	@Secured("ROLE_ADMIN")
	@PutMapping("/caja/{id}")
	public ResponseEntity<?> updateCaja(@Valid @RequestBody Caja caja,
			BindingResult result,
			@PathVariable Long idCaja){
		Caja cajaUpd= null;
		Map<String, Object> response = new HashMap<>();
		//Se validan errorres que se reciben del request
		if(result.hasErrors()) {
			List<String> errores = result.getFieldErrors()
					.stream().map(err->"El campo "+err.getField()+" "+err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		Caja cajaAct = cajaService.findById(idCaja);
		//Se valida si existe la caja
		if(cajaAct == null) {
			response.put("mensaje", "La caja ID: ".concat(idCaja.toString()).concat(" no existe en la Base de datos."));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			//Se stean modificaciones
			cajaAct.setFechaopen(caja.getFechaopen());
			cajaAct.setFechaclose(caja.getFechaclose());
			cajaAct.setFechamod(caja.getFechamod());
			cajaAct.setVenta(caja.getVenta());
			cajaAct.setMercadopago(caja.getMercadopago());
			cajaAct.setIniciocaja(caja.getIniciocaja());
			cajaAct.setDiferencia(caja.getDiferencia());
			cajaAct.setEstado(caja.getEstado());
			cajaAct.setObservacion(caja.getObservacion());
			cajaAct.setCliente(caja.getCliente());
			//Se guarda en base de datos
			cajaUpd = cajaService.saveCaja(cajaAct);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al modificar La caja con Id: ".concat(idCaja.toString()).concat(" en la base de datos."));
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La caja se modifico con exito.");
		response.put("caja", cajaUpd);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	//Metodo para eliminar caja
	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("/caja/{id}")
	public ResponseEntity<?> deleteCaja(@PathVariable Long idCaja){
		Map<String, Object> response = new HashMap<>();
		try {
			cajaService.deleteCaja(idCaja);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la caja ID: "
					.concat(idCaja.toString())
					.concat(" No existe en la base de datos."));
			response.put("error", e.getMessage().concat(": ")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La caja ha sido eliminada con exito.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/caja/estado")
	public Boolean estadoCuenta(){
		return cajaService.countByEstadoCaja("Abierto")>0? true:false;
	}
	
	@GetMapping("/caja/buscacliente")
	public Integer buscaCliente(){
		Caja caja = cajaService.buscarXEstado("Abierto");  
		return (caja!=null)?caja.getCliente().getId():0;
	}
	
	//Metodo de cierre de una caja
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/caja/cerrarcaja/{diferencia}")
	public Integer cerrarCaja(@PathVariable Double diferencia){
		//Se busca la caja en base de datos con estatus Abierto
		Caja caja = cajaService.buscarXEstado("Abierto");
		Double venta = 0D;
		Double mercadopago = 0D;
		//Se añade diferencia a la caja
		caja.setDiferencia(diferencia);
		//Se obtiene el cliente de la caja
		Cliente cliente = caja.getCliente();
		//Se recorre las facturas del cliente
		for (Factura item : cliente.getFacturas()) {
			item.setCaja(caja);
			//El cliente deja de pertenecer a la caja
			item.setCliente(null);
			venta += item.getTotal();
			mercadopago += item.getMercadopago() != null ? item.getMercadopago(): 0D;
			facturaService.modificoFactura(item);
		}
		//Proceso para el registro de retiros en caja
		Double totalRetiros = 0D;
		//Se recorren los retiros del cliente
		for (RetiroCaja retiro : cliente.getRetirosCaja()) {
			//Se desvincula el retiro del cliente y se le setea la caja
			retiro.setCliente(null);
			retiro.setCaja(caja);
			retiroService.saveRetiroCaja(retiro);
			totalRetiros += retiro.getMonto();
		}		
		caja.setRetiros(totalRetiros);
		caja.setMercadopago(mercadopago);
		caja.setVenta(venta);
		caja.setEstado("Cerrado");
		caja.setFechaclose(new Date());
		cajaService.saveCaja(caja);
		cajachicaService.registroDiferenciaCaja(caja, diferencia);
		return caja.getCliente().getId();
	}
	
	//Se recuperan total de diferencias por mes
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/caja/diferenciasxmes")
	public ResponseEntity<?> getGastos( ) {
		Double diferencias = null;
		//Se crea Map para el envio de mensaje de error en el ResponseEntity
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			//Se recupera las perdidas por mes
			diferencias = cajaService.findDiferenciasXMes();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//Se valida si hay perdidas es null y se maneja el error
		if(diferencias == null || diferencias == 0) {
			response.put("mensaje", "No existen diferencias en el mes en la Base de Datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
		}
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<Double>(diferencias, HttpStatus.OK);
	}
}
