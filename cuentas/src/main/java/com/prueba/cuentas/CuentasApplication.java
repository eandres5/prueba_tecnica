package com.prueba.cuentas;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CuentasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuentasApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {return new RestTemplate();}

	@Bean
	public OpenAPI apiInfo() {
		return new OpenAPI()
				.info(new Info()
						.title("API - Cuentas")
						.description("Documentación de microservicios")
						.version("1.0.0"));
	}

}
