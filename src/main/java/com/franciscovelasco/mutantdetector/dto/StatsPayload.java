package com.franciscovelasco.mutantdetector.dto;

/**
 * DTO expuesto en /stats para compartir conteos y ratio.
 * Se deja inmutable para simplificar el cacheo futuro.
 *
 * @param mutantCount cantidad de ADN mutante registrado
 * @param humanCount cantidad de ADN humano registrado
 * @param ratio división segura entre mutantes y humanos
 * @author Francisco Velasco (Legajo 51141)
 */
public record StatsPayload(long mutantCount, long humanCount, double ratio) {
}

