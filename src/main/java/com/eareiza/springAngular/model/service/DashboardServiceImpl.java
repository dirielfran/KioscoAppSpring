
package com.eareiza.springAngular.model.service;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eareiza.springAngular.DTO.EstadisticasComparativaDTO;
import com.eareiza.springAngular.interfaces.IDashboardService;
import com.eareiza.springAngular.model.repository.GastosRepository;
import com.eareiza.springAngular.model.repository.IFacturaRepository;
import com.eareiza.springAngular.model.repository.IPerdidaRepository;
import com.eareiza.springAngular.model.repository.IProductoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class DashboardServiceImpl implements IDashboardService{

	@Autowired
	private IFacturaRepository facturasRepo;
	
	@Autowired
	private GastosRepository gastosRepo;
	
	@Autowired
	private IPerdidaRepository perdidaRepo;
	
	@Autowired
	private IProductoRepository productoRepo;
	
	LocalDate fecha = LocalDate.now();
	
	@Override
	public List<Object[]> findVentasUlt7() {
		Integer mes = fecha.getMonthValue();
		Integer anio = fecha.getYear();
		return facturasRepo.findVentasXUlt30(mes, anio);
	}
	
	@Override
	public List<Object[]> findGastosUlt7() {
		int mes = fecha.getMonthValue();
		int anio = fecha.getYear();
		return gastosRepo.findGastosXUlt30(mes, anio);
	}
	

	public List<Object[]> findGananciasUlt7() {
		LocalDate desde = fecha.withDayOfMonth(1);
		LocalDate hasta = fecha.withDayOfMonth(fecha.lengthOfMonth());
		return facturasRepo.findGananciasXUlt7(desde, hasta);
	}
	
	@Override
	public List<Object[]> findPerdidasXSemana() {
		int mes = fecha.getMonthValue();
		int anio = fecha.getYear();
		return perdidaRepo.findPerdidasXUlt30(mes, anio);
	}

	@Override
	public List<Object[]> findProductosTopMes() {
		LocalDate desde = fecha.withDayOfMonth(1);
		LocalDate hasta = fecha.withDayOfMonth(fecha.lengthOfMonth());
		cotizacionDolar();
		return productoRepo.findProductosTop(desde, hasta);
	}
	
	@Override
	public Double findPatrimonio() {
		return productoRepo.findPatrimonio(cotizacionDolar());
	}	
	
	@Override
	public List<EstadisticasComparativaDTO> findComparativa() {
		Integer anio = fecha.getYear();
		Integer mes = fecha.getMonthValue();
		List<Double> ganancias = facturasRepo.findGananciasXMes();
		List<Double> ventas = facturasRepo.findVentasXMes(anio);
		List<Double> gastos = gastosRepo.findGastosXMes(anio, "Gasto");
		List<EstadisticasComparativaDTO> dtoList = new ArrayList<>();
		for (int i = 0; i < mes; i++) {
			Double ganancia = ganancias.get(i);
			Double venta = ventas.get(i);
			Double gasto = gastos.get(i);
			EstadisticasComparativaDTO dto = new EstadisticasComparativaDTO(i, venta, ganancia, gasto );
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	public List<EstadisticasComparativaDTO> findTopX3(){
		LocalDate fechaInic = LocalDate.now();
		fechaInic = fechaInic.withDayOfMonth(1);
		List<Integer> ids = productoRepo.findTopId(fechaInic, fecha);
		fechaInic = fechaInic.minusMonths(2);
		List<EstadisticasComparativaDTO> dtoList = new ArrayList<>();
		List<Object[]> estadisticas = productoRepo.findT0pX3(fechaInic, fecha, ids);
		for (Object[] obj : estadisticas) {
			String nombre = (String) obj[0];
			Double ganancia = (Double) obj[1];
			EstadisticasComparativaDTO dto = new EstadisticasComparativaDTO(nombre, ganancia);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	public Double cotizacionDolar() {
		Double dolar = 0D;
		RestTemplate restTemplate = new RestTemplate();
		String  call= restTemplate.getForObject("https://www.dolarsi.com/api/api.php?type=valoresprincipales",String.class);
		// Obtain Array
        JsonArray gsonArr = JsonParser.parseString(call).getAsJsonArray();
        for (JsonElement obj : gsonArr) {
            JsonObject gsonObj = obj.getAsJsonObject();
            JsonObject gsonObj1 = gsonObj.get("casa").getAsJsonObject();
            if(gsonObj1.get("nombre").getAsString().equals("Dolar Blue")) {
            	NumberFormat nf = NumberFormat.getInstance();
            	try {
					dolar = nf.parse(gsonObj1.get("venta").getAsString()).doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
		}
        return dolar;
	}
	
}
