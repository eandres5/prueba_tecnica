package com.prueba.clientes;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@OpenAPIDefinition(
		info = @Info(
				title = "Cliente Service API",
				version = "1.0.0",
				description = "Microservicio para gestión de clientes bancarios"),
		servers = {
				@Server(url = "http://localhost:8081", description = "Development Server"),
				@Server(url = "http://cliente-service:8081", description = "Docker Server")
		}
)
public class ClientesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientesApplication.class, args);
	}

}
