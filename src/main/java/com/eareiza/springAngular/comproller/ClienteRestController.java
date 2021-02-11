package com.eareiza.springAngular.comproller;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eareiza.springAngular.interfaces.IClienteService;
import com.eareiza.springAngular.interfaces.IUploadFileService;
import com.eareiza.springAngular.model.entity.Cliente;
import com.eareiza.springAngular.model.entity.Region;

@RestController
@RequestMapping("/api")
//Se configuran los dominios permitidos, soporta una lista d dominios
//Se pueden especificar los metodos permitidos, las cabeceras
@CrossOrigin(origins= {"http://localhost:4200","*"})
public class ClienteRestController {
	
	@Autowired 
	private IClienteService clienteServ;
	@Autowired
	private IUploadFileService uploadService;
	
	@GetMapping("/clientes")
	public List<Cliente> index(){
		return clienteServ.findAll();
	}
	
	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> index(@PathVariable("page") Integer pagina){
		Pageable page = PageRequest.of(pagina, 4);
		//return clienteServ.findAll(page);
		return clienteServ.buscarXEstado(page);
	}
	
	//Se recupera cliente por id
	//Se le añade seguridad a los endpoint por url
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> showCliente(@PathVariable Integer id) {
		Cliente cliente = null;
		//Se crea Map para el envio de mensaje de error en el ResponseEntity
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			//Se recupera el cliente por el id
			cliente = clienteServ.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//Se valida si el cliente es null y se maneja el error
		if(cliente == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la Base de Datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	
	//Se crea Cliente
	//Se le añade seguridad a los endpoint por url
	@Secured("ROLE_ADMIN")
	@PostMapping("/clientes")
	public ResponseEntity<?> saveCliente(@Valid @RequestBody Cliente cliente, BindingResult result) {
		Cliente clienteNew = null;
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
			//Se crea el cliente por medio de la clase de servicio
			clienteNew = clienteServ.save(cliente);
		} catch (DataAccessException e) {
			//Se crean mensajes de error y se añaden all map
			response.put("mensaje", "Error al insertar el cliente en la Base de Datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			//Se crea respuesta enviando los mensajes del error y el estatus
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//Se agrega mensaje success al mapa
		response.put("mensaje", "El cliente se ha registrado");
		//Se agrega obj cliente creado al mapa
		response.put("cliente", clienteNew);		
		//Se retorna respuesta añadiendo mapa y estatus
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	//Update de Cliente 
	//Se le añade seguridad a los endpoint por url
	@Secured("ROLE_ADMIN")
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> updateCliente(@Valid @RequestBody Cliente cliente,BindingResult result, @PathVariable("id") Integer idCliente) {
		//Se crea map que se añadira al response
		Map<String, Object> response= new HashMap<>();
		if(result.hasErrors()) {
			List<String> errores = result.getFieldErrors()
									.stream()
									.map(err -> "El campo '"+err.getField()+" "+err.getDefaultMessage())
									.collect(Collectors.toList());
			response.put("errores", errores);
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		//Se busca cliente a modificar
		Cliente clienteActual = clienteServ.findById(idCliente);
		Cliente clienteUpd = null;

		//Se valida si es null el cliente
		if(clienteActual == null) {
			response.put("mensaje", "El cliente ID: ".concat(idCliente.toString().concat(" no existe en la Base de Datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		//Se manejan los errores con DataAccessException
		try {
			//Se modifica cliente
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setFecha(cliente.getFecha());
			clienteActual.setRegion(cliente.getRegion());
			//Se guarda el cliente
			clienteUpd = clienteServ.save(clienteActual);			
		} catch (DataAccessException e) {
			//Se crean mensajes de error y se añaden all map
			response.put("mensaje", "Error al modificar el cliente con ID ".concat(idCliente.toString()).concat(" en la Base de Datos"));
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			//Se crea respuesta enviando los mensajes del error y el estatus
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		//Se agrega mensaje success al mapa
		response.put("mensaje", "El cliente se modificado con exito");
		//Se agrega obj cliente creado al mapa
		response.put("cliente", clienteUpd);		
		//Se retorna respuesta añadiendo mapa y estatus
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
	//delete de Cliente 
	//Se le añade seguridad a los endpoint por url
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		Map<String, Object> response = new HashMap<>();
		try {
			Cliente cliente = clienteServ.findById(id);
			//Se borra foto para que no quede almacenada
			String fotoAnterior = cliente.getFoto();
			uploadService.eliminar(fotoAnterior);
			clienteServ.delete(id);
		} catch (DataAccessException e) {
			//Se crean mensajes de error y se añaden all map
			response.put("mensaje", "Error al eliminar el cliente con ID ".concat(id.toString()).concat(" en la Base de Datos"));
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			//Se crea respuesta enviando los mensajes del error y el estatus
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El Cliente ha sido eliminado con exito");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	//Metodo que obtiene el file del cliente
	//Se le añade seguridad a los endpoint por url
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/clientes/upload")
	public ResponseEntity<?> uploadArchivo(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Integer idCliente){
		Map<String, Object> response = new HashMap<>();
		
		//Se valida si el archivo esta vacio
		if(!archivo.isEmpty()) {
			Cliente cliente = clienteServ.findById(idCliente);
			String nombreArchivo=null;
			try {			
				nombreArchivo = uploadService.copiar(archivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen del cliente");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			//Se borra foto anterior para que no quede almacenada
			String fotoAnterior = cliente.getFoto();
			
			uploadService.eliminar(fotoAnterior);
			cliente.setFoto(nombreArchivo);
			clienteServ.save(cliente);
			response.put("cliente", cliente);
			response.put("mensaje", "Has subido correctamente la imagen: "+nombreArchivo);
		}
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
		
		Resource recurso = null;
		
		try {
			recurso = uploadService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		//Se pasa el attachmen para que se pueda descargar el archivo
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+recurso.getFilename()+"\"");
		
		return new ResponseEntity<Resource>(recurso,cabecera, HttpStatus.OK);
	}
	
	//Se le añade seguridad a los endpoint por url
	@Secured("ROLE_ADMIN")
	@GetMapping("/clientes/regiones")
	public List<Region> listaRegiones(){
		return clienteServ.findAllRegiones();
	}
}
