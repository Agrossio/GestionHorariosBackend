package com.asj.gestionhorarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionhorariosApplication {
	public static void main(String[] args) {

		SpringApplication.run(GestionhorariosApplication.class, args);
		System.out.println("\n---------        Bienvenido a la API de Gestión de Horarios      ---------\n\nDOC: http://localhost:8080/swagger-ui/#/\n");

	}
}
