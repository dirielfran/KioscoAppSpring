package com.eareiza.springAngular.comproller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
//Se configuran los dominios permitidos, soporta una lista d dominios
//Se pueden especificar los metodos permitidos, las cabeceras
@CrossOrigin(origins= {"http://localhost:4200","*"})
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	@Secured({"ROLE_ADMIN"})
	@GetMapping(value="/encriptacion")
	public ResponseEntity<?> pruebaEncripta() {
		String password ="danger120-";
		String encriptado = encoder.encode(password);
		System.out.println("Password encriptado: "+encriptado);
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "Se ha encriptado "+encriptado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
