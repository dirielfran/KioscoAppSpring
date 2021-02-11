package com.eareiza.springAngular.comproller;

import java.util.Date;
import java.util.Random;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.eareiza.springAngular.model.entity.mongoDB.Mensaje;

@Controller
public class ChatController {
	
	private String[] colores = {"red" ,"blue","green", "brown", "orange", "purple"};
	
	//Metodo donde se indica el destino de los mensajes del chat
	@MessageMapping("/mensaje")
	//Se configura el nombre del evento al que se le envia el mensaje
	@SendTo("/chat/mensaje")
	public Mensaje recibeMensaje(Mensaje mensaje) {
		mensaje.setFecha(new Date().getTime());
		if(mensaje.getTipo().equals("NUEVO_USUARIO")) {
			mensaje.setColor(colores[new Random().nextInt(colores.length)]);
			mensaje.setTexto("nuevo usuario");
		}	
		return mensaje; 
	}
	
	//Metodo que informa si un usuario esta escribiendo
	@MessageMapping("/escribiendo")
	@SendTo("/chat/escribiendo")
	public String escribiendo(String username) {
		return username.concat(" esta escribiendo...");
	}
}
