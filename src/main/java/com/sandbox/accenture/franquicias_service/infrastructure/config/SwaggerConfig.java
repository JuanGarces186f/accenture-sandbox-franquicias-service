package com.sandbox.accenture.franquicias_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Franquicias Service API")
                        .description("API REST para gestión de franquicias, sucursales y productos — Prueba Técnica Accenture")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Accenture Sandbox")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/franquicias-service")
                                .description("Entorno de desarrollo — http://localhost:8080"),
                        new Server()
                                .url("https://franquicias-service-prod-209503874304.us-central1.run.app/franquicias-service")
                                .description("Entorno GCP — PostgreSQL Cloud SQL")
                ));
    }
}
