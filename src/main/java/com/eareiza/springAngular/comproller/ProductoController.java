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
import org.springframework.mail.javamail.JavaMailSender;
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

import com.eareiza.springAngular.interfaces.IProductoService;
import com.eareiza.springAngular.model.entity.Producto;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200","*"})
public class ProductoController {
	
	//Importante hacer la inyección de dependencia de JavaMailSender:
    @Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private IProductoService productoServ;
	
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/producto/filtrarProducto/{term}")
	@ResponseStatus(HttpStatus.OK)
	public List<Producto> filtrarProducto(@PathVariable("term") String termino){
		return productoServ.findProductoByNombre(termino);
	}
	
	@GetMapping("/producto")
	public List<Producto> productos(){
		return productoServ.getProductos();
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/producto/page/{page}")
	public Page<Producto> productosPage(@PathVariable Integer page){
		Pageable pagina = PageRequest.of(page, 5);
		return productoServ.findAll(pagina);
	}
	
	
	//Metodo para obtener producto por id
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/producto/{id}")
	public ResponseEntity<?> showProducto(@PathVariable Long id){
		Producto producto = null;
		Map<String, Object> response = new HashMap<>();
		try {
			producto = productoServ.getbyId(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(producto == null) {
			response.put("mensaje", "El producto Id: ".concat(id.toString())
					.concat(" no existe en la base de datos."));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Producto>(producto, HttpStatus.OK);
	}

	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/producto")
	public ResponseEntity<?> saveProducto(@Valid @RequestBody Producto producto, BindingResult result){
		Producto productoNew = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			List<String> errores = result.getFieldErrors()
				//Se convierte en stream
				.stream()
				//Cada error se convierte en un string
				.map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage())
				//Se convierte en una lista
				.collect(Collectors.toList());
			response.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		try {
			productoNew = productoServ.saveProducto(producto);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar el producto.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje","El cliente se ha registrado.");
		response.put("producto", productoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	//Metodo para la modificacion de un producto
	@Secured("ROLE_ADMIN")
	@PutMapping("/producto/{id}")
	public ResponseEntity<?> updateProducto(@Valid @RequestBody Producto producto,
			BindingResult result,
			@PathVariable Long id){
		Producto productoUpd= null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			List<String> errores = result.getFieldErrors()
					.stream().map(err->"El campo "+err.getField()+" "+err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		Producto productoAct = productoServ.getbyId(id);
		//Se valida si el producto existe
		if(productoAct== null) {
			response.put("mensaje", "El producto ID: ".concat(id.toString()).concat(" no existe en la Base de datos."));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
		}
		try {
			//Se envia email por modificacion del producto
//			EnvioMail enviomail = new EnvioMail();
//			String body ="Se ha registrado un gasto con los siguientes detalles: <br> \n"
//					+ "Nombre: "+producto.getNombre()+"\n"
//					+ "Id: "+producto.getId()+"\n"
//					+ "Fecha: "+producto.getCreateAt()+"\n"
//					+ "Precio: "+producto.getPrecio()+"\n"
//					+ "Proveedor: "+producto.getProveedor()+"\n"
//					+ "Minimo: "+producto.getMinimo()+"\n";
//			String subject="El producto "+producto.getNombre()+" se ha modificado con exito.";
//			//enviomail.sendEmail("dirielfran@gmail.com", subject, body);
//			
//			SimpleMailMessage email = new SimpleMailMessage();
//	        
//	        //recorremos la lista y enviamos a cada cliente el mismo correo
//	        email.setTo("dirielfran@gmail.com");
//	        email.setSubject(subject);
//	        email.setText(body);
//	        
//	        mailSender.send(email);	
			
			//Se modifican los atributos modificados y se guarda
			productoAct.setNombre(producto.getNombre());
			productoAct.setPrecio(producto.getPrecio());
			productoAct.setProveedor(producto.getProveedor());
			productoAct.setExistencia(producto.getExistencia());
			productoAct.setMinimo(producto.getMinimo());
			productoUpd = productoServ.saveProducto(productoAct);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al modificar el cliente con Id: ".concat(id.toString()).concat(" en la base de datos."));
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El producto se modifico con exito.");
		response.put("producto", productoUpd);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	//metodo para borrar un Producto
	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("/producto/{id}")
	public ResponseEntity<?> deleteProducto(@PathVariable Long id){
		Map<String, Object> response = new HashMap<>();
		try {
			productoServ.deleteProducto(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el producto ID: "
					.concat(id.toString())
					.concat(" No existe en la base de datos."));
			response.put("error", e.getMessage().concat(": ")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El producto ha sido eliminado con exito.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
