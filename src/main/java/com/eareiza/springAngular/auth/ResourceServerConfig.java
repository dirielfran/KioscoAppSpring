package com.eareiza.springAngular.auth;

import java.util.Arrays;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
//permite que nuestra aplicación se comporte como un  servidor de recursos 
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Override
	public void configure(HttpSecurity http) throws Exception {
		//Se permite a todos el accesso por el metodo get a "/api/clientes"
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/clientes", "/api/personas","/api/clientes/page/**","/api/clientes/page/**", "/api/uploads/img/**", "/images/**").permitAll()
		.antMatchers("/chat-webSocket/**").permitAll()
		.anyRequest().authenticated();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		//Se crea instancia de CorsConfiguration
		CorsConfiguration conf = new CorsConfiguration();
		//Permitir dominio
		conf.setAllowedOrigins(Arrays.asList("http://localhost:4200", "*"));
		//Se configuran todos los verbos o metodos que seran permitidos en el backend
		conf.setAllowedMethods(Arrays.asList("GET","POST", "DELETE", "PUT", "OPTIONS"));
		//Se permitesn las credenciales
		conf.setAllowCredentials(true);
		//Se configuran las cabeceras que se permiten
		conf.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
		
		//Se registra la configuracion para todas las rutas
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", conf);
		return source;
	}
	
	@Bean 
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
}
