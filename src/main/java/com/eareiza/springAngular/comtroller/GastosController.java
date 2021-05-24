package com.eareiza.springAngular.comtroller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eareiza.springAngular.interfaces.ICajachicaService;
import com.eareiza.springAngular.interfaces.IGastosService;
import com.eareiza.springAngular.model.entity.Cajachica;
import com.eareiza.springAngular.model.entity.Gastos;
import com.eareiza.springAngular.model.service.EnvioMail;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200","*"})
public class GastosController {

	//Importante hacer la inyección de dependencia de JavaMailSender:
    @Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private IGastosService serviceGastos;
	
	@Autowired
	private ICajachicaService cajaService;
	
	@Value("${empleosapp.ruta.imagenes}")
	private String ruta;

	
	@GetMapping("/gastos/page/{page}")
	public Page<Gastos> indicePaginado(@PathVariable("page") Integer pagina) {
		Pageable page = PageRequest.of(pagina, 5);
		// se llama al metodo de la implementacion
		return serviceGastos.getGastos(page);
	}
	
	@GetMapping("/gastos")
	public Cajachica caja(){
		return cajaService.findTopByOrderByIdDesc();
	}
	
	//Se crea Cliente
	//Se le añade seguridad a los endpoint por url
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@PostMapping("/gastos")
	public ResponseEntity<?> saveGastos(@Valid @RequestBody Gastos gasto, BindingResult result,
										@RequestParam(value = "user") String user) {
		Gastos gastoNew = null;
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
			//Se crea Gasto por medio de la clase de servicio
			gasto.setUsuario(user);
			gasto.setClasificacion("Gasto");
			gastoNew = serviceGastos.guardar(gasto);
		} catch (DataAccessException e) {
			//Se crean mensajes de error y se añaden all map
			response.put("mensaje", "Error al insertar Gasto en la Base de Datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			//Se crea respuesta enviando los mensajes del error y el estatus
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//Se agrega mensaje success al mapa
		response.put("mensaje", "El Gasto se ha registrado");
		//Se agrega obj cliente creado al mapa
		response.put("gasto", gastoNew);
		//envioMail(gastoNew, "registrado");
		//Se retorna respuesta añadiendo mapa y estatus
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	

	
	//delete de Gasto
	//Se le añade seguridad a los endpoint por url
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@DeleteMapping("/gastos/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			serviceGastos.borrarGasto(id);
		} catch (DataAccessException e) {
			//Se crean mensajes de error y se añaden all map
			response.put("mensaje", "Error al eliminar el gato con ID ".concat(id.toString()).concat(" en la Base de Datos"));
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			//Se crea respuesta enviando los mensajes del error y el estatus
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El Gasto ha sido eliminado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	//Se recupera gasto por id
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/gastos/{id}")
	public ResponseEntity<?> showGasto(@PathVariable Long id) {
		Gastos gasto = null;
		//Se crea Map para el envio de mensaje de error en el ResponseEntity
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			//Se recupera el inventario por el id
			gasto = serviceGastos.buscarPorId(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//Se valida si el inventario es null y se maneja el error
		if(gasto == null) {
			response.put("mensaje", "El gasto ID: ".concat(id.toString().concat(" no existe en la Base de Datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
		}
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<Gastos>(gasto, HttpStatus.OK);
	}
	
	
	//Se recuperan total de ganancias
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/gastos/ganancias")
	public ResponseEntity<?> getGanancias( ) {
		Double ganancias = null;
		//Se crea Map para el envio de mensaje de error en el ResponseEntity
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			//Se recupera el inventario por el id
			ganancias = serviceGastos.findGanancias();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//Se valida si el inventario es null y se maneja el error
		if(ganancias == null) {
			response.put("mensaje", "No existen ganancias para este mes en la Base de Datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
		}
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<Double>(ganancias, HttpStatus.OK);
	}
	
	//Se recuperan total de gastos por mes
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/gastos/gastosxmes")
	public ResponseEntity<?> getGastos( ) {
		Double gastos = null;
		//Se crea Map para el envio de mensaje de error en el ResponseEntity
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			//Se recupera los gastos por mes
			gastos = serviceGastos.findGastosxmes();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//Se valida si el inventario es null y se maneja el error
		if(gastos == null) {
			response.put("mensaje", "No existen gastos en el mes en la Base de Datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
		}
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<Double>(gastos, HttpStatus.OK);
	}
	
	
	
	// declarar un conversor de tipos fcha para que sea manejado por el
	// controlador antes de almacenarlo en el bean
	// Aplica para todos los tipos de variable, tipo fecha
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	private void envioMail(Gastos gasto, String tipo) {
		//Se envia email por modificacion del producto
		EnvioMail enviomail = new EnvioMail();
		String body ="Se ha "+tipo+" un gasto con los siguientes detalles: <br> \n"
				+ "Id: "+gasto.getId()+"\n"
				+ "Fecha: "+gasto.getFechaFact()+"\n"
				+ "Precio: "+gasto.getUsuario()+"\n"
				+ "Proveedor: "+gasto.getClasificacion()+"\n";
		String subject="El gasto "+gasto.getId()+" se ha "+tipo+" con exito.";
		//enviomail.sendEmail("dirielfran@gmail.com", subject, body);
		
		SimpleMailMessage email = new SimpleMailMessage();
        
        //recorremos la lista y enviamos a cada cliente el mismo correo
        email.setTo("dirielfran@gmail.com");
        email.setSubject(subject);
        email.setText(body);
        
        mailSender.send(email);
	}
}
