package com.eareiza.springAngular.comproller;

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

import com.eareiza.springAngular.interfaces.IPerdidaService;
import com.eareiza.springAngular.model.entity.Perdida;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200","*"})
public class PerdidaRestController {
	
	@Autowired
	private IPerdidaService perdidaService;
	


	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/perdida")
	public List<Perdida> getPerdidas(){
		return perdidaService.findAll();
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/perdida/page/{page}")
	public Page<Perdida> cajaPage(@PathVariable Integer page){
		Pageable pagina = PageRequest.of(page, 5);
		return perdidaService.finAll(pagina);
	}
	
	//Se recupera perdida por id
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/perdida/{id}")
	public ResponseEntity<?> showPerdida(@PathVariable("id") Long idPerdida){
		Perdida perdida = null;
		Map<String, Object> response = new HashMap<>();
		try {
			//Se recupera la perdida de la clase de servicio
			perdida = perdidaService.findById(idPerdida);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			//Se envia error a la vista
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//Se valida si la caja esta en null
		if(perdida == null) {
			response.put("mensaje", "La perdida Id: ".concat(idPerdida.toString())
					.concat(" no existe en la base de datos."));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Perdida>(perdida, HttpStatus.OK);
	}
	
	//Metodo para la creacion de caja
	@Secured({"ROLE_ADMIN"})
	@PostMapping("/perdida")
	public ResponseEntity<?> savePerdida(@Valid @RequestBody Perdida perdida, BindingResult result){
		Perdida perdidaNew = null;
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
			perdidaNew = perdidaService.savePerdida(perdida);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar la Perdida.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje","La perdida se ha registrado.");
		response.put("perdida", perdidaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	
	//Metodo para modificar Perdida
	@Secured("ROLE_ADMIN")
	@PutMapping("/perdida/{id}")
	public ResponseEntity<?> updatePerdida(@Valid @RequestBody Perdida perdida,
			BindingResult result,
			@PathVariable("id") Long idPerdida){
		Perdida perdidaUpd= null;
		Map<String, Object> response = new HashMap<>();
		//Se validan errorres que se reciben del request
		if(result.hasErrors()) {
			List<String> errores = result.getFieldErrors()
					.stream().map(err->"El campo "+err.getField()+" "+err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		Perdida perdidaAct = perdidaService.findById(idPerdida);
		//Se valida si existe
		if(perdidaAct == null) {
			response.put("mensaje", "La perdida ID: ".concat(idPerdida.toString()).concat(" no existe en la Base de datos."));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			//Se stean modificaciones
			
			//Pendiente
			
			//Se guarda en base de datos
			perdidaUpd = perdidaService.savePerdida(perdidaAct);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al modificar La perdida con Id: ".concat(idPerdida.toString()).concat(" en la base de datos."));
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La perdida se modifico con exito.");
		response.put("perdida", perdidaUpd);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	//Metodo para eliminar perdida
	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("/perdida/{id}")
	public ResponseEntity<?> deletePerdida(@PathVariable("id") Long idPerdida){
		Map<String, Object> response = new HashMap<>();
		try {
			perdidaService.deletePerdida(idPerdida);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la perdida ID: "
					.concat(idPerdida.toString())
					.concat(" No existe en la base de datos."));
			response.put("error", e.getMessage().concat(": ")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La perdida ha sido eliminada con exito.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	//Se recuperan total de perdidas por mes
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/perdida/perdidasxmes")
	public ResponseEntity<?> getGastos( ) {
		Double perdidas = null;
		//Se crea Map para el envio de mensaje de error en el ResponseEntity
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			//Se recupera las perdidas por mes
			perdidas = perdidaService.findPerdidasXMes();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//Se valida si hay perdidas es null y se maneja el error
		if(perdidas == null || perdidas == 0) {
			response.put("mensaje", "No existen perdidas en el mes en la Base de Datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
		}
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<Double>(perdidas, HttpStatus.OK);
	}

}
