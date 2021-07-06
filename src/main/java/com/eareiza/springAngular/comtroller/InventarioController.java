package com.eareiza.springAngular.comtroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.eareiza.springAngular.utileria.Utileria;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eareiza.springAngular.interfaces.IGastosService;
import com.eareiza.springAngular.interfaces.IInventarioService;
import com.eareiza.springAngular.interfaces.IProductoService;
import com.eareiza.springAngular.interfaces.IUsuariosService;
import com.eareiza.springAngular.model.entity.Gastos;
import com.eareiza.springAngular.model.entity.Inventario;
import com.eareiza.springAngular.model.entity.ItemInventario;
import com.eareiza.springAngular.model.entity.Producto;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200","*"})
public class InventarioController {

	@Autowired
	IInventarioService inventarioService;
	@Autowired
	IProductoService productoService;
	@Autowired
	IUsuariosService usuarioService;
	@Autowired
	IGastosService gastosService;

	private static final Utileria util = new Utileria();
	
	@GetMapping("/inventarios")
	public List<Inventario> cajas(){
		return inventarioService.findAll();
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/inventarios/page/{page}")
	public Page<Inventario> cajaPage(@PathVariable Integer page){
		Pageable pagina = PageRequest.of(page, 5);
		return inventarioService.findAll(pagina);
	}	
	
	//Se recupera inventario por id
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/inventarios/{id}")
	public ResponseEntity<?> showInventario(@PathVariable Long id) {
		Inventario inventario = null;
		//Se crea Map para el envio de mensaje de error en el ResponseEntity
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			//Se recupera el inventario por el id
			inventario = inventarioService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//Se valida si el inventario es null y se maneja el error
		if(inventario == null) {
			response.put("mensaje", "El inventario ID: ".concat(id.toString().concat(" no existe en la Base de Datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<Inventario>(inventario, HttpStatus.OK);
	}
	
	
	/*
	 * Metodo para guardar inventario
	 */
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/inventarios")
	public ResponseEntity<?> saveInventario(@Valid @RequestBody Inventario inventario, BindingResult result,
											@RequestParam(value = "user") String user, 
											@RequestParam(value = "consignacion") Boolean consignacion){
		Inventario inventarioNew = null;
		Map<String, Object> response = new HashMap<>();
		//Se validan errores del requestbody
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
			//Se recupera el usuario que recupero del get
			inventario.setUsuario(usuarioService.findByUsername(user));
			//Se recorren los items del inventario
			for (ItemInventario item : inventario.getItems()) {
				//A la existencia actual se le suma la existencia del producto mas lo que se añade del inventario
				Double existenciaActual = item.getProducto().getExistencia()+item.getStockadd();
				//Se setea al stockinicial del inventario la existencia del producto
				item.setStockinicial(item.getProducto().getExistencia());
				item.setExistencia(item.getStockadd());
				item.setConsignacion(consignacion);
				item.setUser(util.getUsuarioAuth());
				//Se modifica existencia y precio del producto segun el inventario cargado
				Producto producto = item.getProducto();
				producto.setExistencia(existenciaActual);
				producto.setPrecio(item.getPrecioventa());
				productoService.saveProducto(producto);
			}
			//Validacion, se crea gasto en caso de que no sea un inventario a consignacion
			//Se guarda el inventario
			inventarioNew = inventarioService.save(inventario);
			if (!consignacion) {
				
				//Se crea el gasto
				gastosService.crearGastoInventario(inventario, "Inventario", null, null);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar el inventario.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			System.out.println(e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje","El inventario se ha registrado.");
		response.put("inventario", inventarioNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	//Metodo para la modificacion del inventario
	@Secured("ROLE_ADMIN")
	@PutMapping("/inventarios/{id}")
	public ResponseEntity<?> updateInventario(@Valid @RequestBody Inventario inventario,
			BindingResult result,
			@PathVariable Long idInventario){
		Inventario inventarioUpd= null;
		Map<String, Object> response = new HashMap<>();
		//Mnejo de errores 
		if(result.hasErrors()) {
			List<String> errores = result.getFieldErrors()
					.stream().map(err->"El campo "+err.getField()+" "+err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		Inventario inventarioAct = inventarioService.findById(idInventario);
		if(inventarioAct== null) {
			response.put("mensaje", "El inventario ID: ".concat(idInventario.toString()).concat(" no existe en la Base de datos."));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			//Modificaciones del inventario
			inventarioAct.setItems(inventario.getItems());
			inventarioAct.setUsuario(inventario.getUsuario());
			inventarioUpd = inventarioService.save(inventarioAct);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al modificar el inventario con Id: ".concat(idInventario.toString()).concat(" en la base de datos."));
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El inventario se modifico con exito.");
		response.put("inventario", inventarioUpd);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	//Metodo para la eliminacion de un inventario
	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("/inventarios/{idInventario}")
	public ResponseEntity<?> deleteInventario(@PathVariable Long idInventario){

		Map<String, Object> response = new HashMap<>();
		try {
			Gastos gasto = gastosService.buscarPorInventario(idInventario);
			if(gasto != null )gastosService.borrarGasto(gasto.getId());
			inventarioService.delete(idInventario);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el inventario ID: "
					.concat(idInventario.toString())
					.concat(" No existe en la base de datos."));
			response.put("error", e.getMessage().concat(": ")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El inventario ha sido eliminada con exito.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}

