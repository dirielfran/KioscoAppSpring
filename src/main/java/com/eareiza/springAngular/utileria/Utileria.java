package com.eareiza.springAngular.utileria;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;


public class Utileria {
	
	public static String guardarArchivo(MultipartFile multiPart, String ruta) {
		// Obtenemos el nombre original del archivo.
		String nombreOriginal = multiPart.getOriginalFilename();
		nombreOriginal = nombreOriginal.replace(" ", "-");
		String nombreFinal = randomAlphaNumeric(8)+nombreOriginal;
		try {
			// Formamos el nombre del archivo para guardarlo en el disco duro.
			File imageFile = new File(ruta+ nombreFinal);
			System.out.println("Archivo: " + imageFile.getAbsolutePath());
			//Guardamos fisicamente el archivo en HD.
			multiPart.transferTo(imageFile);
			return nombreFinal;
		} catch (IOException e) {
			System.out.println("Error " + e.getMessage());
			return null;
		}
	}
	
	
	
	//Metodo que retorna una lista de n cantidad de fechas
	public static List<String> listaFecha(int count){
		//Se crea un objeto para el formateo de la fecha
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		//fecha desde hoy
		Date startDate = new Date();
		//Creando la instancia en el calendario
		Calendar cal = Calendar.getInstance();
		//Se a�aden al calendario los proximos N dias desde hoy
		cal.add(Calendar.DAY_OF_MONTH,count); 
		//Se utiliza metodo del calendario para obtener la fecha final
		Date endDate = cal.getTime();
		
		//Se crea un objeto GregorianCalendar
		GregorianCalendar gcal = new GregorianCalendar();
		//Seteamos al GregorianCalendar la fecha de inicio
		gcal.setTime(startDate);
		//Se crea una lista que contiene n cantidad de fechas 
		List<String> nextDays = new ArrayList<String>();
		//Se crea un bucle,mientras la fecha de inicio no sea mayor  
		while(!gcal.getTime().after(endDate)) {
			//Se crea objeto Date y se agrega fecha del item
			Date d = gcal.getTime();
			//Se le suma 1 a la fecha de inicio
			gcal.add(Calendar.DATE, 1);
			//A�ade a la lista la fecha
			nextDays.add(sdf.format(d));
		}
		return nextDays;
	}
	
	//Metodo para generar una cadena de longitud N de caracteres aleatorios
	public static String randomAlphaNumeric(int count) {
		//Se colocan los todos los caracteres  
		String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		//Se crea un contructor de string
		StringBuilder builder = new StringBuilder();
		//Creamos bucle que recorre los caracteres
		while(count-- != 0) {
			//Se instancia una variable con un caracter random
			int character = (int)(Math.random()* CARACTERES.length());
			//Se le agrega el character al constructor de string
			builder.append(CARACTERES.charAt(character));
		}
		return builder.toString();
	}

	//Se obtiene usuario authenticado
	public String getUsuarioAuth(){
		String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}
}
