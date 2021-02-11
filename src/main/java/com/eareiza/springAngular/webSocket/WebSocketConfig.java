package com.eareiza.springAngular.webSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//Se añade anotacion de configuracion 
@Configuration
//Habilitacion del webSocketMessage
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	//Se registra EndPoint
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//Se configura punto de acceso desde el cliente (Angular)
		registry.addEndpoint("/chat-webSocket")
		//Se configura el cors para cliente (Angular)
		.setAllowedOrigins("http://localhost:4200")
		// Configuracion para que nos permita el protocolo http 
		.withSockJS();
	}
	//Se configuran prefijos en el envio de la mensajeria
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//Se configura prefijo para los eventos
		registry.enableSimpleBroker("/chat/");
		//se configura prefijo de destino
		registry.setApplicationDestinationPrefixes("/app");
	}
	
	

}
