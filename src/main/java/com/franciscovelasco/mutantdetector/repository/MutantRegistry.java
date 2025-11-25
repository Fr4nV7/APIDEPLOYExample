package com.franciscovelasco.mutantdetector.repository;

import com.franciscovelasco.mutantdetector.entity.MutantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio que centraliza las consultas de ADN procesado.
 * Prefiero nombres custom para despegarlo del ejemplo original.
 *
 * @author Francisco Velasco (Legajo 51141)
 */
public interface MutantRegistry extends JpaRepository<MutantEntity, Long> {

    Optional<MutantEntity> findByGenomeHash(String genomeHash);

    long countByMutantTrue();

    long countByMutantFalse();
}

