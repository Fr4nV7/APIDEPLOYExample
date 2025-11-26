package com.franciscovelasco.mutantdetector.service;

import com.franciscovelasco.mutantdetector.dto.StatsPayload;
import com.franciscovelasco.mutantdetector.repository.MutantRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DnaMetricsServiceTest {

    @Mock
    private MutantRegistry mutantRegistry;

    @InjectMocks
    private DnaMetricsService dnaMetricsService;

    @Test
    @DisplayName("Debe calcular estadísticas correctamente cuando hay mutantes y humanos")
    void fetchStats_shouldReturnCorrectStats_WhenMutantsAndHumansExist() {
        when(mutantRegistry.countByMutantTrue()).thenReturn(40L);
        when(mutantRegistry.countByMutantFalse()).thenReturn(100L);

        StatsPayload stats = dnaMetricsService.fetchStats();

        assertThat(stats.mutantCount()).isEqualTo(40);
        assertThat(stats.humanCount()).isEqualTo(100);
        assertThat(stats.ratio()).isEqualTo(0.4);
    }

    @Test
    @DisplayName("Debe calcular ratio 0 cuando no hay humanos")
    void fetchStats_shouldReturnZeroRatio_WhenNoHumans() {
        when(mutantRegistry.countByMutantTrue()).thenReturn(10L);
        when(mutantRegistry.countByMutantFalse()).thenReturn(0L);

        StatsPayload stats = dnaMetricsService.fetchStats();

        assertThat(stats.mutantCount()).isEqualTo(10);
        assertThat(stats.humanCount()).isEqualTo(0);
        assertThat(stats.ratio()).isEqualTo(10.0);
    }

    @Test
    @DisplayName("Debe calcular ratio correctamente cuando no hay mutantes")
    void fetchStats_shouldReturnZeroRatio_WhenNoMutants() {
        when(mutantRegistry.countByMutantTrue()).thenReturn(0L);
        when(mutantRegistry.countByMutantFalse()).thenReturn(50L);

        StatsPayload stats = dnaMetricsService.fetchStats();

        assertThat(stats.mutantCount()).isEqualTo(0);
        assertThat(stats.humanCount()).isEqualTo(50);
        assertThat(stats.ratio()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Debe calcular ratio 1.0 cuando hay igual cantidad de mutantes y humanos")
    void fetchStats_shouldReturnRatioOne_WhenEqualCounts() {
        when(mutantRegistry.countByMutantTrue()).thenReturn(25L);
        when(mutantRegistry.countByMutantFalse()).thenReturn(25L);

        StatsPayload stats = dnaMetricsService.fetchStats();

        assertThat(stats.mutantCount()).isEqualTo(25);
        assertThat(stats.humanCount()).isEqualTo(25);
        assertThat(stats.ratio()).isEqualTo(1.0);
    }

    @Test
    @DisplayName("Debe retornar 0 en todos los campos cuando no hay datos")
    void fetchStats_shouldReturnZeros_WhenNoData() {
        when(mutantRegistry.countByMutantTrue()).thenReturn(0L);
        when(mutantRegistry.countByMutantFalse()).thenReturn(0L);

        StatsPayload stats = dnaMetricsService.fetchStats();

        assertThat(stats.mutantCount()).isEqualTo(0);
        assertThat(stats.humanCount()).isEqualTo(0);
        assertThat(stats.ratio()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Debe calcular ratio correctamente con números grandes")
    void fetchStats_shouldCalculateRatio_WithLargeNumbers() {
        when(mutantRegistry.countByMutantTrue()).thenReturn(1000L);
        when(mutantRegistry.countByMutantFalse()).thenReturn(5000L);

        StatsPayload stats = dnaMetricsService.fetchStats();

        assertThat(stats.mutantCount()).isEqualTo(1000);
        assertThat(stats.humanCount()).isEqualTo(5000);
        assertThat(stats.ratio()).isEqualTo(0.2);
    }
}

