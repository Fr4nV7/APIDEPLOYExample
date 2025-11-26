# Diagrama de Secuencia - Mutant Detector API

## Flujo: POST /mutant/

```
Cliente                    Controller              DnaAnalyzerService          MutantRegistry          H2 Database
  |                            |                            |                          |                        |
  |--POST /mutant/ {dna:[]}-->|                            |                          |                        |
  |                            |                            |                          |                        |
  |                            |--inspectGenome(dna)------>|                          |                        |
  |                            |                            |                          |                        |
  |                            |                            |--normalize(dna)          |                        |
  |                            |                            |                          |                        |
  |                            |                            |--enforceSquareMatrix()  |                        |
  |                            |                            |                          |                        |
  |                            |                            |--hashGenome(dna)         |                        |
  |                            |                            |                          |                        |
  |                            |                            |--findByGenomeHash(hash)->|                        |
  |                            |                            |                          |--SELECT * FROM...------>|
  |                            |                            |                          |<--Optional<Entity>------|
  |                            |                            |<--Optional.empty()-------|                        |
  |                            |                            |                          |                        |
  |                            |                            |--buildMatrix(dna)         |                        |
  |                            |                            |                          |                        |
  |                            |                            |--detectMutations(matrix)  |                        |
  |                            |                            |  |                       |                        |
  |                            |                            |  |--exploreRows()         |                        |
  |                            |                            |  |--exploreColumns()     |                        |
  |                            |                            |  |--explorePrimaryDiagonals()|                      |
  |                            |                            |  |--exploreSecondaryDiagonals()|                  |
  |                            |                            |  |                       |                        |
  |                            |                            |<--true (mutant)----------|                        |
  |                            |                            |                          |                        |
  |                            |                            |--persistResult()         |                        |
  |                            |                            |                          |                        |
  |                            |                            |--save(entity)----------->|                        |
  |                            |                            |                          |--INSERT INTO...------->|
  |                            |                            |                          |<--Entity saved---------|
  |                            |                            |<--true-------------------|                        |
  |                            |<--true (mutant)-----------|                          |                        |
  |<--200 OK-------------------|                            |                          |                        |
```

## Flujo: GET /stats

```
Cliente                    Controller              DnaMetricsService           MutantRegistry          H2 Database
  |                            |                            |                          |                        |
  |--GET /stats--------------->|                            |                          |                        |
  |                            |                            |                          |                        |
  |                            |--fetchStats()------------>|                          |                        |
  |                            |                            |                          |                        |
  |                            |                            |--countByMutantTrue()----->|                        |
  |                            |                            |                          |--SELECT COUNT(*)...--->|
  |                            |                            |                          |<--40-------------------|
  |                            |                            |<--40---------------------|                        |
  |                            |                            |                          |                        |
  |                            |                            |--countByMutantFalse()--->|                        |
  |                            |                            |                          |--SELECT COUNT(*)...--->|
  |                            |                            |                          |<--100-----------------|
  |                            |                            |<--100--------------------|                        |
  |                            |                            |                          |                        |
  |                            |                            |--calculate ratio (40/100)|                        |
  |                            |                            |                          |                        |
  |                            |                            |--new StatsPayload(40, 100, 0.4)|                 |
  |                            |<--StatsPayload------------|                          |                        |
  |<--200 OK {count_mutant_dna:40, count_human_dna:100, ratio:0.4}--|                            |                          |                        |
```

## Flujo: POST /mutant/ (con caché)

```
Cliente                    Controller              DnaAnalyzerService          MutantRegistry          H2 Database
  |                            |                            |                          |                        |
  |--POST /mutant/ {dna:[]}-->|                            |                          |                        |
  |                            |                            |                          |                        |
  |                            |--inspectGenome(dna)------>|                          |                        |
  |                            |                            |                          |                        |
  |                            |                            |--normalize(dna)          |                        |
  |                            |                            |                          |                        |
  |                            |                            |--hashGenome(dna)         |                        |
  |                            |                            |                          |                        |
  |                            |                            |--findByGenomeHash(hash)->|                        |
  |                            |                            |                          |--SELECT * FROM...------>|
  |                            |                            |                          |<--Optional<Entity>------|
  |                            |                            |<--Optional.of(entity)----|                        |
  |                            |                            |                          |                        |
  |                            |                            |--entity.isMutant()       |                        |
  |                            |                            |                          |                        |
  |                            |<--true (cached)------------|                          |                        |
  |<--200 OK-------------------|                            |                          |                        |
```

## Notas del Diagrama

1. **Validación temprana**: El Controller valida el JSON usando `@Valid` antes de llamar al servicio.
2. **Caché**: Si el hash del ADN ya existe en la BD, se retorna el resultado sin recalcular.
3. **Persistencia**: Solo se guarda en BD si el ADN no fue analizado previamente.
4. **Early Termination**: El algoritmo de detección se detiene cuando encuentra 2 secuencias.
5. **Transaccionalidad**: La operación `inspectGenome` es transaccional para garantizar consistencia.

## Conversión a PDF

Para convertir este archivo a PDF, puedes usar:
- **Pandoc**: `pandoc DIAGRAMA_SECUENCIA.md -o DIAGRAMA_SECUENCIA.pdf`
- **Markdown a PDF** (extensión VS Code)
- **Online converters**: https://www.markdowntopdf.com/

