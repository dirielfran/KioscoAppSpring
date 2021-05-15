package com.eareiza.springAngular.comproller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eareiza.springAngular.DTO.EstadisticasComparativaDTO;
import com.eareiza.springAngular.interfaces.ICajachicaService;
import com.eareiza.springAngular.interfaces.IDashboardService;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins= {"http://localhost:4200","*"})
public class DashboardController {
	
	@Autowired
	private IDashboardService dashboardService;	
	
	@Autowired
	private ICajachicaService cajaChicaService;
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/ventas")
	public ResponseEntity<?> showVentasXSemana() {
		List<Object[]> ventas = null;
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			//Se recupera la venta  por el id
			ventas = dashboardService.findVentasUlt7();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//Se valida si venta es null y se maneja el error
		if(ventas.isEmpty()) {
			response.put("mensaje", "No hay ventas para las fechas indicadas en el reporte");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.FORBIDDEN);
		}
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<List<Object[]>>(ventas, HttpStatus.OK);
	}

	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/gastos")
	public ResponseEntity<?> showGastosXSemana() {
		List<Object[]> gastos = null;
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			//Se recupera el gasto por el id
			gastos = dashboardService.findGastosUlt7();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//Se valida si el gasto es null y se maneja el error
		if(gastos.isEmpty()) {
			response.put("mensaje", "No hay gastos para las fechas indicadas en el reporte");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.FORBIDDEN);
		}
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<List<Object[]>>(gastos, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/varios")
	public ResponseEntity<?> showVariosXSemana() {
		List<Object[]> gastos = null;
		List<Object[]> ventas = null;
		List<Object[]> ganancias = null;
		List<Object[]> perdidas = null;
		List<Object[]> productosTop = null;
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			//Se recupera data 
			gastos = dashboardService.findGastosUlt7();
			ventas = dashboardService.findVentasUlt7();
			ganancias = dashboardService.findGananciasUlt7();
			perdidas = dashboardService.findPerdidasXSemana();
			//productosTop = dashboardService.findProductosTopMes();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//Se valida si los datos son null y se maneja el error
		if(gastos.isEmpty() || ventas.isEmpty() || ganancias.isEmpty()  ) {
			response.put("mensaje", "No hay registros para las fechas indicadas en el reporte");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.FORBIDDEN);
		}
		response.put("ventas", ventas);
		response.put("gastos", gastos);
		response.put("ganancias", ganancias);
		response.put("perdidas", perdidas);
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/productosTop")
	public ResponseEntity<?> showProductosTop() {
		List<Object[]> productosTop = null;
		Double patrimonio = 0D;
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			//Se recupera los 10 productos mas vendidos
			productosTop = dashboardService.findProductosTopMes();
			//Se recupera el patrimonio
			patrimonio = dashboardService.findPatrimonio();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//Se valida si el cliente es null y se maneja el error
		if(productosTop.isEmpty()) {
			response.put("mensaje", "No hay gastos para las fechas indicadas en el reporte");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.FORBIDDEN);
		}
		response.put("productosTop", productosTop);
		response.put("patrimonio", patrimonio);
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
	
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/compara")
	public ResponseEntity<?> showComparativa() {
		List<EstadisticasComparativaDTO>  estadistica= null;
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			//Se recupera los 10 productos mas vendidos
			estadistica = dashboardService.findComparativa();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//Se valida si es null y se maneja el error
		if(estadistica.isEmpty()) {
			response.put("mensaje", "No hay gastos para las fechas indicadas en el reporte");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.FORBIDDEN);
		}
		response.put("estadistica", estadistica);
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/prodTopX3")
	public ResponseEntity<?> showProductosTopX3() {
		List<EstadisticasComparativaDTO>  estadistica= null;
		Map<String, Object> response = new HashMap<>();
		//Se maneja el error de base datos con el obj de spring DataAccessExceptions
		try {
			//Se recupera los 10 productos mas vendidos
			estadistica = dashboardService.findTopX3();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error a realizar la consulta en la base de Datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//Se valida si es null y se maneja el error
		if(estadistica.isEmpty()) {
			response.put("mensaje", "No hay gastos para las fechas indicadas en el reporte");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.FORBIDDEN);
		}
		response.put("estadistica", estadistica);
		//En caso de existir se retorna el obj y el estatus del mensaje
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/transferencia")
	public ResponseEntity<?> transferirDinero(@RequestBody Map<String,String> transferencia){
		Map<String, Object> response = new HashMap<>();
		System.out.println(transferencia.toString());
		cajaChicaService.transaferenciaSaldo(transferencia);
		response.put("mensaje", "La caja se modifico con exito.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
