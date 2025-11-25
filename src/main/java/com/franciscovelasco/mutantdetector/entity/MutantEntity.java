package com.franciscovelasco.mutantdetector.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad encargada de almacenar cada análisis de ADN realizado.
 * Persistimos el hash para evitar recalcular secuencias previamente evaluadas.
 *
 * @author Francisco Velasco (Legajo 51141)
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dna_audit")
public class MutantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "genome_hash", nullable = false, unique = true, length = 64)
    private String genomeHash;

    @Column(name = "matrix_snapshot", nullable = false, length = 2048)
    private String matrixSnapshot;

    @Column(name = "is_mutant", nullable = false)
    private boolean mutant;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}

