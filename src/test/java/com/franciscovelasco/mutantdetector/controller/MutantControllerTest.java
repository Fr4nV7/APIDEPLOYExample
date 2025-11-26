package com.franciscovelasco.mutantdetector.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /mutant/ debe retornar 200 OK cuando es mutante")
    void testCheckMutant_ReturnOk_WhenIsMutant() throws Exception {
        String jsonRequest = """
            {
              "dna": ["ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"]
            }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant/ debe retornar 403 Forbidden cuando NO es mutante")
    void testCheckMutant_ReturnForbidden_WhenIsNotMutant() throws Exception {
        String jsonRequest = """
            {
              "dna": ["ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"]
            }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant/ debe retornar 400 cuando el DNA es inválido (carácter no permitido)")
    void testCheckMutant_ReturnBadRequest_WhenInvalidCharacter() throws Exception {
        String jsonRequest = """
            {
              "dna": ["ATXC", "CAGT", "TTAT", "AGAC"]
            }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant/ debe retornar 400 cuando la matriz no es cuadrada")
    void testCheckMutant_ReturnBadRequest_WhenNotSquareMatrix() throws Exception {
        String jsonRequest = """
            {
              "dna": ["ATGC", "CAGT", "TTAT"]
            }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant/ debe retornar 400 cuando la matriz es muy pequeña")
    void testCheckMutant_ReturnBadRequest_WhenMatrixTooSmall() throws Exception {
        String jsonRequest = """
            {
              "dna": ["ATG", "CAG", "TTA"]
            }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /stats debe retornar estadísticas correctas")
    void testStats_ReturnStats() throws Exception {
        // Primero insertamos algunos datos
        String mutantDna = """
            {
              "dna": ["ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"]
            }
            """;
        mockMvc.perform(post("/mutant/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mutantDna));

        String humanDna = """
            {
              "dna": ["ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"]
            }
            """;
        mockMvc.perform(post("/mutant/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(humanDna));

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").exists())
                .andExpect(jsonPath("$.count_human_dna").exists())
                .andExpect(jsonPath("$.ratio").exists());
    }

    @Test
    @DisplayName("GET /stats debe retornar ratio 0 cuando no hay humanos")
    void testStats_ReturnZeroRatio_WhenNoHumans() throws Exception {
        String mutantDna = """
            {
              "dna": ["ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"]
            }
            """;
        mockMvc.perform(post("/mutant/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mutantDna));

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(1))
                .andExpect(jsonPath("$.count_human_dna").value(0))
                .andExpect(jsonPath("$.ratio").value(1.0));
    }

    @Test
    @DisplayName("POST /mutant/ debe retornar 400 cuando el campo dna está vacío")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsEmpty() throws Exception {
        String jsonRequest = """
            {
              "dna": []
            }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }
}

