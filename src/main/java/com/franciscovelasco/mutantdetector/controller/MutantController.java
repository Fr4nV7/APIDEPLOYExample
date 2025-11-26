package com.franciscovelasco.mutantdetector.controller;

import com.franciscovelasco.mutantdetector.dto.GenomeInput;
import com.franciscovelasco.mutantdetector.dto.StatsPayload;
import com.franciscovelasco.mutantdetector.service.DnaAnalyzerService;
import com.franciscovelasco.mutantdetector.service.DnaMetricsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Puerta de entrada HTTP. Aquí solo orquestamos servicios y traducimos códigos de estado.
 * Separar responsabilidades mantiene el controlador ligero y verificable.
 *
 * @author Francisco Velasco (Legajo 51141)
 */
@RestController
@Validated
@RequiredArgsConstructor
public class MutantController {

    private final DnaAnalyzerService dnaAnalyzerService;
    private final DnaMetricsService dnaMetricsService;

    @PostMapping("/mutant/")
    public ResponseEntity<Void> analyzeGenome(@Valid @RequestBody GenomeInput genomeInput) {
        boolean mutant = dnaAnalyzerService.inspectGenome(genomeInput.dnaRows());
        // Forzamos 403 para humanos porque el cliente necesita distinguir ambos casos sin payload adicional.
        return mutant
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsPayload> stats() {
        return ResponseEntity.ok(dnaMetricsService.fetchStats());
    }
}

