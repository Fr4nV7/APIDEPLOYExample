package com.franciscovelasco.mutantdetector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Representa la carga útil recibida en /mutant/ con la matriz de ADN.
 * El campo JSON se llama "dna" según el requerimiento del examen.
 *
 * @param dnaRows secuencia NxN de caracteres A,T,C,G
 * @author Francisco Velasco (Legajo 51141)
 */
public record GenomeInput(
        @JsonProperty("dna")
        @NotEmpty(message = "Se requiere al menos una fila de ADN")
        @Size(min = 4, message = "El ADN debe tener una dimensión mínima de 4x4")
        List<
                @NotEmpty(message = "Cada fila debe contener caracteres")
                @Pattern(regexp = "^[ATCG]+$", message = "Solo se admiten caracteres A,T,C,G")
                String> dnaRows
) {
}

