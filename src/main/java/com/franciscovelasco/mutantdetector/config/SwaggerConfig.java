package com.franciscovelasco.mutantdetector.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para documentación automática de la API.
 *
 * @author Francisco Velasco (Legajo 51141)
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mutant Detector API")
                        .version("1.0.0")
                        .description("API REST para detectar mutantes analizando secuencias de ADN. " +
                                "Desarrollado como examen técnico de MercadoLibre Backend Developer.")
                        .contact(new Contact()
                                .name("Francisco Velasco")
                                .email("francisco.velasco@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}

