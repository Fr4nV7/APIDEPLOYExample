package com.franciscovelasco.mutantdetector.service;

import com.franciscovelasco.mutantdetector.entity.MutantEntity;
import com.franciscovelasco.mutantdetector.exception.GenomeFormatException;
import com.franciscovelasco.mutantdetector.repository.MutantRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio que encapsula la lógica para determinar si un ADN es mutante.
 * Se prioriza frenar la búsqueda una vez que se detectan dos secuencias válidas.
 *
 * @author Francisco Velasco (Legajo 51141)
 */
@Service
@RequiredArgsConstructor
public class DnaAnalyzerService {

    private static final int WINDOW = 4;
    private static final int REQUIRED_MATCHES = 2;
    private static final String ALLOWED_SYMBOLS = "ATCG";

    private final MutantRegistry mutantRegistry;

    /**
     * Analiza la matriz, reutiliza resultados cacheados y persiste el nuevo estudio.
     *
     * @param dnaRows matriz NxN provista por el cliente
     * @return true cuando se detectan dos o más secuencias válidas
     */
    @Transactional
    public boolean inspectGenome(List<String> dnaRows) {
        List<String> normalized = normalize(dnaRows);
        enforceSquareMatrix(normalized);

        String genomeHash = hashGenome(normalized);
        // Si ya existe el hash evitamos recalcular el algoritmo completo.
        return mutantRegistry.findByGenomeHash(genomeHash)
                .map(MutantEntity::isMutant)
                .orElseGet(() -> persistResult(normalized, genomeHash));
    }

    private boolean persistResult(List<String> normalized, String genomeHash) {
        char[][] matrix = buildMatrix(normalized);
        boolean mutant = detectMutations(matrix);
        // Guardamos la foto exacta analizada para futuras auditorías.
        MutantEntity entity = MutantEntity.builder()
                .genomeHash(genomeHash)
                .matrixSnapshot(String.join(",", normalized))
                .mutant(mutant)
                .createdAt(LocalDateTime.now())
                .build();
        mutantRegistry.save(entity);
        return mutant;
    }

    private List<String> normalize(List<String> dnaRows) {
        if (dnaRows == null || dnaRows.isEmpty()) {
            throw new GenomeFormatException("El ADN no puede estar vacío");
        }
        // Se homogeneiza la entrada para no depender del formato del cliente.
        return dnaRows.stream()
                .map(row -> row == null ? "" : row.trim().toUpperCase())
                .toList();
    }

    private void enforceSquareMatrix(List<String> dnaRows) {
        int dimension = dnaRows.size();
        if (dimension < WINDOW) {
            throw new GenomeFormatException("La matriz debe ser al menos 4x4");
        }
        dnaRows.forEach(row -> {
            if (row.length() != dimension) {
                throw new GenomeFormatException("La matriz debe ser NxN");
            }
            int cursor = 0;
            while (cursor < row.length()) {
                if (ALLOWED_SYMBOLS.indexOf(row.charAt(cursor)) == -1) {
                    throw new GenomeFormatException("Solo se admiten caracteres A,T,C,G");
                }
                cursor++;
            }
        });
    }

    private char[][] buildMatrix(List<String> dnaRows) {
        int size = dnaRows.size();
        char[][] matrix = new char[size][size];
        int rowIndex = 0;
        while (rowIndex < size) {
            matrix[rowIndex] = dnaRows.get(rowIndex).toCharArray();
            rowIndex++;
        }
        return matrix;
    }

    private boolean detectMutations(char[][] grid) {
        SequenceTracker tracker = new SequenceTracker();
        exploreRows(grid, tracker);
        if (tracker.shouldStop()) {
            return true;
        }
        exploreColumns(grid, tracker);
        if (tracker.shouldStop()) {
            return true;
        }
        explorePrimaryDiagonals(grid, tracker);
        if (tracker.shouldStop()) {
            return true;
        }
        exploreSecondaryDiagonals(grid, tracker);
        return tracker.shouldStop();
    }

    private void exploreRows(char[][] grid, SequenceTracker tracker) {
        int row = 0;
        while (row < grid.length && !tracker.shouldStop()) {
            inspectLine(grid[row], tracker);
            row++;
        }
    }

    private void exploreColumns(char[][] grid, SequenceTracker tracker) {
        int col = 0;
        int size = grid.length;
        while (col < size && !tracker.shouldStop()) {
            char[] column = new char[size];
            int row = 0;
            while (row < size) {
                column[row] = grid[row][col];
                row++;
            }
            inspectLine(column, tracker);
            col++;
        }
    }

    private void explorePrimaryDiagonals(char[][] grid, SequenceTracker tracker) {
        int limit = grid.length - WINDOW;
        int row = 0;
        while (row <= limit && !tracker.shouldStop()) {
            inspectDiagonal(grid, row, 0, 1, 1, tracker);
            row++;
        }
        int col = 1;
        while (col <= limit && !tracker.shouldStop()) {
            inspectDiagonal(grid, 0, col, 1, 1, tracker);
            col++;
        }
    }

    private void exploreSecondaryDiagonals(char[][] grid, SequenceTracker tracker) {
        int limit = grid.length - WINDOW;
        int row = 0;
        int lastColumn = grid.length - 1;
        while (row <= limit && !tracker.shouldStop()) {
            inspectDiagonal(grid, row, lastColumn, 1, -1, tracker);
            row++;
        }
        int col = lastColumn - 1;
        while (col >= WINDOW - 1 && !tracker.shouldStop()) {
            inspectDiagonal(grid, 0, col, 1, -1, tracker);
            col--;
        }
    }

    private void inspectDiagonal(char[][] grid, int startRow, int startCol, int rowStep, int colStep,
                                 SequenceTracker tracker) {
        char[] diagonal = extractDiagonal(grid, startRow, startCol, rowStep, colStep);
        if (diagonal.length >= WINDOW) {
            inspectLine(diagonal, tracker);
        }
    }

    private char[] extractDiagonal(char[][] grid, int row, int col, int rowStep, int colStep) {
        int size = grid.length;
        StringBuilder builder = new StringBuilder();
        int currentRow = row;
        int currentCol = col;
        while (currentRow >= 0 && currentRow < size && currentCol >= 0 && currentCol < size) {
            builder.append(grid[currentRow][currentCol]);
            currentRow += rowStep;
            currentCol += colStep;
        }
        return builder.toString().toCharArray();
    }

    private void inspectLine(char[] line, SequenceTracker tracker) {
        if (line.length < WINDOW) {
            return;
        }
        int streak = 1;
        int pointer = 1;
        while (pointer < line.length && !tracker.shouldStop()) {
            if (line[pointer] == line[pointer - 1]) {
                streak++;
                if (streak == WINDOW) {
                    tracker.registerMatch();
                    streak = 1;
                }
            } else {
                streak = 1;
            }
            pointer++;
        }
    }

    private String hashGenome(List<String> dnaRows) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String payload = String.join("|", dnaRows);
            byte[] hashBytes = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(hashBytes.length * 2);
            for (byte hashByte : hashBytes) {
                builder.append(String.format("%02x", hashByte));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible en la JVM actual", e);
        }
    }

    // Contador simple para cortar la búsqueda una vez cumplido el umbral.
    private static final class SequenceTracker {
        private int sequences;

        private void registerMatch() {
            sequences++;
        }

        private boolean shouldStop() {
            return sequences >= REQUIRED_MATCHES;
        }
    }
}

