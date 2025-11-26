package com.franciscovelasco.mutantdetector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO expuesto en /stats para compartir conteos y ratio.
 * Los nombres de campos siguen el formato snake_case según requerimiento.
 *
 * @param mutantCount cantidad de ADN mutante registrado
 * @param humanCount cantidad de ADN humano registrado
 * @param ratio división segura entre mutantes y humanos
 * @author Francisco Velasco (Legajo 51141)
 */
public record StatsPayload(
        @JsonProperty("count_mutant_dna")
        long mutantCount,
        @JsonProperty("count_human_dna")
        long humanCount,
        double ratio
) {
}

