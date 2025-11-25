package com.franciscovelasco.mutantdetector.service;

import com.franciscovelasco.mutantdetector.entity.MutantEntity;
import com.franciscovelasco.mutantdetector.exception.GenomeFormatException;
import com.franciscovelasco.mutantdetector.repository.MutantRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DnaAnalyzerServiceTest {

    @Mock
    private MutantRegistry mutantRegistry;

    @InjectMocks
    private DnaAnalyzerService dnaAnalyzerService;

    @Test
    @DisplayName("Detecta mutantes y persiste el resultado cuando no existe cache")
    void inspectGenome_shouldReturnTrueForMutant() {
        when(mutantRegistry.findByGenomeHash(anyString())).thenReturn(Optional.empty());

        boolean result = dnaAnalyzerService.inspectGenome(mutantDna());

        assertThat(result).isTrue();
        verify(mutantRegistry).save(any(MutantEntity.class));
    }

    @Test
    @DisplayName("Devuelve humano cuando no se alcanzan dos secuencias")
    void inspectGenome_shouldReturnFalseForHuman() {
        when(mutantRegistry.findByGenomeHash(anyString())).thenReturn(Optional.empty());

        boolean result = dnaAnalyzerService.inspectGenome(humanDna());

        assertThat(result).isFalse();
        verify(mutantRegistry).save(any(MutantEntity.class));
    }

    @Test
    @DisplayName("Omite procesamiento cuando la secuencia ya fue calculada")
    void inspectGenome_shouldUseCacheWhenAvailable() {
        when(mutantRegistry.findByGenomeHash(anyString()))
                .thenReturn(Optional.of(MutantEntity.builder().mutant(true).build()));

        boolean result = dnaAnalyzerService.inspectGenome(mutantDna());

        assertThat(result).isTrue();
        verify(mutantRegistry, never()).save(any());
    }

    @Test
    @DisplayName("Valida que la matriz sea NxN antes de procesar")
    void inspectGenome_shouldValidateSquareMatrix() {
        List<String> invalidMatrix = List.of("ATGC", "CAGT", "TTAT");

        assertThrows(GenomeFormatException.class, () -> dnaAnalyzerService.inspectGenome(invalidMatrix));
        verify(mutantRegistry, never()).save(any());
    }

    private List<String> mutantDna() {
        return List.of(
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        );
    }

    private List<String> humanDna() {
        return List.of(
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        );
    }
}


