package com.franciscovelasco.mutantdetector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación que inicializa el contexto de Spring Boot.
 * Elegí un nombre distinto para remarcar que el servicio vigila el análisis génico completo.
 *
 * @author Francisco Velasco (Legajo 51141)
 */
@SpringBootApplication
public class GenomeSentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenomeSentinelApplication.class, args);
    }
}


