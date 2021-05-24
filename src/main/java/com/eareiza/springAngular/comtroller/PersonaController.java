package com.eareiza.springAngular.comtroller;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eareiza.springAngular.interfaces.IPersonaService;
import com.eareiza.springAngular.model.entity.Persona;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins= {"http://localhost:4200","*"})
public class PersonaController {
	
	@Autowired
	private IPersonaService personaService;
	

	@GetMapping("/personas")
	public List<Persona> index(){
		return personaService.findAll();
	}
	
	@GetMapping("/personas/page/{page}")
	public Page<Persona> index(@PathVariable("page") Integer pagina){
		Pageable page = PageRequest.of(pagina, 4);
		return personaService.findAll(page);
	}
	
	//Metodo para recuperar Persona por Id
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/personas/{id}")
	public ResponseEntity<?> showPersona(@PathVariable Long id) {
		Persona persona= null;
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			persona = personaService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if( persona == null) {
			response.put("mensaje", "La persona ID: ".concat(id.toString().concat(" no existe en la Base de Datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Persona>(persona, HttpStatus.OK);
	}
	
	//Metodo para modificacion de persona 
	@Secured("ROLE_ADMIN")
	@PutMapping("/personas/{id}")
	public ResponseEntity<?> updatePersona(@Valid @RequestBody Persona persona, BindingResult result, @PathVariable("id") Long id) {
		//Se crea map que se añadira al response
		Map<String, Object> response= new HashMap<>();
		if(result.hasErrors()) {
			List<String> errores = result.getFieldErrors()
									.stream()
									.map(err -> "El campo "+err.getField()+" "+err.getDefaultMessage())
									.collect(Collectors.toList());
			response.put("errores", errores);
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
		}
		Persona personaNew = personaService.findById(id);
		Persona personaUpd = null;
		if(personaNew == null) {
			response.put("mensaje", "La persona ID: ".concat(id.toString().concat(" no existe en la Base de Datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			personaNew.setPerApellido(persona.getPerApellido());
			personaNew.setPerNombre(persona.getPerNombre());
			personaNew.setPerFechaNacimiento(persona.getPerFechaNacimiento());
			personaNew.setPerNumeroDocumento(persona.getPerNumeroDocumento());
			personaNew.setPerTipoDocumento(persona.getPerTipoDocumento());

			personaUpd = personaService.save(personaNew);			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al modificar la Persona con ID ".concat(id.toString()).concat(" en la Base de Datos"));
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			//Se crea respuesta enviando los mensajes del error y el estatus
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		response.put("mensaje", "La persona se ha modificado con exito");
		response.put("persona", personaUpd);		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	//Metodo para la creacion de Persona 
	@Secured("ROLE_ADMIN")
	@PostMapping("/personas")
	public ResponseEntity<?> savePersona(@Valid @RequestBody Persona persona, BindingResult result) {
		Persona personaNew = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			List<String> errores = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);
		}
		//Manejo de errores utilizando el obj de spring DataAccessException
		try {
			personaNew = personaService.save(persona);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar la persona en la Base de Datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La persona se ha registrado");
		response.put("persona", personaNew);		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	

	
	//Metodo para borrar persona Fisica
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/personas/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			personaService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la persona con ID ".concat(id.toString()).concat(" en la Base de Datos"));
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			//Se crea respuesta enviando los mensajes del error y el estatus
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La persona ha sido eliminado con exito");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/personas/filtrarPersona/{term}")
	@ResponseStatus(HttpStatus.OK)
	public List<Persona> filtrarPersona(@PathVariable("term") String termino){
		return personaService.findPersonaByNombre(termino);
	}
	

}
