package com.franciscovelasco.mutantdetector.exception;

/**
 * Error controlado utilizado cuando el ADN no cumple con las reglas NxN.
 * Lanzarlo temprano evita ciclos innecesarios en la búsqueda de secuencias.
 *
 * @author Francisco Velasco (Legajo 51141)
 */
public class GenomeFormatException extends RuntimeException {

    public GenomeFormatException(String message) {
        super(message);
    }
}

