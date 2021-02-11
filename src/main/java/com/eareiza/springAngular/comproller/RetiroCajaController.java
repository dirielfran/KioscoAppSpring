package com.eareiza.springAngular.comproller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eareiza.springAngular.interfaces.IRetiroCajaService;
import com.eareiza.springAngular.model.entity.RetiroCaja;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200","*"})
public class RetiroCajaController {
	
	@Autowired
	IRetiroCajaService retiroService;
	
	@GetMapping("/retiros")
	public List<RetiroCaja> getRetiros(){
		return retiroService.getRetirosCaja();
	}
	
	//Metodo para obtener retiro
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/retiros/{id}")
	public ResponseEntity<?> verRetiro(@PathVariable Long idRetiro){
		RetiroCaja retiroCaja = new RetiroCaja();
		Map<String, Object> response = new HashMap<>();
		try {
			retiroCaja = retiroService.getbyId(idRetiro);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(retiroCaja == null) {
			response.put("mensaje", "El Retiro Id: ".concat(idRetiro.toString())
				.concat(" no existe en la base de datos."));
			new ResponseEntity<Map<String,Object>>(response, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<RetiroCaja>(retiroCaja, HttpStatus.OK);
	}
	
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/retiros")
	public ResponseEntity<?> saveRetiro(@Valid @RequestBody RetiroCaja retiro, BindingResult result){
		RetiroCaja newRetiro = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			List<String> errores = result.getFieldErrors() 
					.stream()
					.map( err -> "El campo '"+ err.getField()+"' "+err.getDefaultMessage())
					.collect(Collectors.toList());
				response.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			newRetiro = retiroService.saveRetiroCaja(retiro);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar retiro");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El retiro se ha registrado");
		response.put("retiroCaja", newRetiro);
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
	
}
