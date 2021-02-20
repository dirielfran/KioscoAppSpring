package com.eareiza.springAngular;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringAngularApplication /*implements CommandLineRunner*/ extends SpringBootServletInitializer{

	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	//Importante hacer la inyección de dependencia de JavaMailSender:
    @Autowired
    private JavaMailSender mailSender;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringAngularApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		String password = "12345";
//		
//		for (int i = 0; i < 4; i++) {
//			String passwordEnc = passwordEncoder.encode(password);
//			System.out.println(passwordEnc);
//		}
//		
//		SimpleMailMessage email = new SimpleMailMessage();
//        
//        //recorremos la lista y enviamos a cada cliente el mismo correo
//        email.setTo("ytobosa@gmail.com");
//        email.setSubject("Prueba");
//        email.setText("Te quiero un mundo aunque tu no me quieras como antes");
//        
//        mailSender.send(email);		
//		
//	}

}
