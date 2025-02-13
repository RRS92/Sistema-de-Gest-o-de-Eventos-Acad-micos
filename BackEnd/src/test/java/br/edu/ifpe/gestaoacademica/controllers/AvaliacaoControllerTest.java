package br.edu.ifpe.gestaoacademica.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(OrderAnnotation.class)
public class AvaliacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private static Long createdAvaliacaoId;
    // Supondo que já exista um evento com id 1 para teste (ou crie-o previamente)
    private final Long dummyEventoId = 5L;
    
    @Test
    @Order(1)
    public void deveCriarAvaliacaoComSucesso() throws Exception {
        String avaliacaoJson = "{"
                + "\"nota\": \"10\","
                + "\"comentario\": \"Ótimo evento!\","
                + "\"idEvento\": " + dummyEventoId + ","
                + "\"participante\": null"
                + "}";
        String response = mockMvc.perform(post("/avaliacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(avaliacaoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        createdAvaliacaoId = objectMapper.readTree(response).get("id").asLong();
    }
    
    @Test
    @Order(2)
    public void deveListarAvaliacoes() throws Exception {
        mockMvc.perform(get("/avaliacoes"))
                .andExpect(status().isOk());
    }
    
    @Test
    @Order(3)
    public void deveListarAvaliacoesPorEvento() throws Exception {
        mockMvc.perform(get("/avaliacoes/evento")
                .param("eventoId", dummyEventoId.toString()))
                .andExpect(status().isOk());
    }
    
    @Test
    @Order(4)
    public void deveAtualizarAvaliacao() throws Exception {
        String updateJson = "{"
                + "\"id\": " + createdAvaliacaoId + ","
                + "\"nota\": \"8\","
                + "\"comentario\": \"Bom evento\","
                + "\"idEvento\": " + dummyEventoId + ","
                + "\"participante\": null"
                + "}";
        mockMvc.perform(put("/avaliacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nota").value("8"))
                .andExpect(jsonPath("$.comentario").value("Bom evento"));
    }
    
    @Test
    @Order(5)
    public void deveInativarAvaliacao() throws Exception {
        mockMvc.perform(delete("/avaliacoes/" + createdAvaliacaoId))
                .andExpect(status().isNoContent());
    }
    
    @Test
    @Order(6)
    public void deveDeletarAvaliacao() throws Exception {
        // Cria uma nova avaliação e em seguida a deleta
        String avaliacaoJson = "{"
                + "\"nota\": \"9\","
                + "\"comentario\": \"Legal\","
                + "\"idEvento\": " + dummyEventoId + ","
                + "\"participante\": null"
                + "}";
        String response = mockMvc.perform(post("/avaliacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(avaliacaoJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long avaliacaoId = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(delete("/avaliacoes/deletar/" + avaliacaoId))
                .andExpect(status().isNoContent());
    }
}
