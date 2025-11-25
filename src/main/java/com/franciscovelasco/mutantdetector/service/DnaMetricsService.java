package com.franciscovelasco.mutantdetector.service;

import com.franciscovelasco.mutantdetector.dto.StatsPayload;
import com.franciscovelasco.mutantdetector.repository.MutantRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio auxiliar encargado de exponer las métricas solicitadas por el enunciado.
 * Mantenerlo separado facilita evolucionar las estadísticas sin tocar la lógica de análisis.
 *
 * @author Francisco Velasco (Legajo 51141)
 */
@Service
@RequiredArgsConstructor
public class DnaMetricsService {

    private final MutantRegistry mutantRegistry;

    public StatsPayload fetchStats() {
        long mutants = mutantRegistry.countByMutantTrue();
        long humans = mutantRegistry.countByMutantFalse();
        double ratio = humans == 0 ? mutants : (double) mutants / humans;
        return new StatsPayload(mutants, humans, ratio);
    }
}

