package br.edu.ifpe.gestaoacademica.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(OrderAnnotation.class)
public class ParticipanteControllerTest {

    @Autowired 
    private MockMvc mockMvc;
    
    @Autowired 
    private ObjectMapper objectMapper;
    
    private static Long createdParticipanteId;
    
  //Testes que buscam o sucesso

    @Test
    @Order(1)
    public void deveCriarParticipante() throws Exception {
        String participanteJson = "{"
            + "\"aluno\": null," 
            + "\"evento\": null," 
            + "\"certificado\": null"
            + "}";
        String response = mockMvc.perform(post("/participantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(participanteJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        createdParticipanteId = objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @Order(2)
    public void deveListarParticipantes() throws Exception {
        mockMvc.perform(get("/participantes"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void deveListarParticipantesPorEvento() throws Exception {
        // Supondo que para o evento com id 1 a lista seja retornada (mesmo que vazia)
        mockMvc.perform(get("/participantes/evento/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void deveInativarParticipante() throws Exception {
        mockMvc.perform(delete("/participantes/" + createdParticipanteId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(5)
    public void deveDeletarParticipante() throws Exception {
        // Cria um novo participante e em seguida deleta-o
        String participanteJson = "{"
            + "\"aluno\": null," 
            + "\"evento\": null," 
            + "\"certificado\": null"
            + "}";
        String response = mockMvc.perform(post("/participantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(participanteJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long participanteId = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(delete("/participantes/deletar/" + participanteId))
                .andExpect(status().isNoContent());
    }

  //Testes que buscam a falha

    @Test
    @Order(6)
    public void deveFalharInativarParticipanteInexistente() throws Exception {
        // Tenta inativar um participante com ID inexistente
        mockMvc.perform(delete("/participantes/9999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(7)
    public void deveFalharDeletarParticipanteInexistente() throws Exception {
        // Tenta deletar definitivamente um participante com ID inexistente
        mockMvc.perform(delete("/participantes/deletar/9999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(8)
    public void deveListarParticipantesPorEventoVazio() throws Exception {
        // Tenta listar participantes para um evento que não possui nenhum participante.
        // Supondo que o evento com ID 9999 não exista ou esteja vazio,
        // o endpoint deve retornar 200 com uma lista vazia.
        mockMvc.perform(get("/participantes/evento/9999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
    
    @Test
    @Order(9)
    public void deveFalharCriarParticipanteComJsonInvalido() throws Exception {
        // Envia um JSON mal formado para a criação de participante.
        String jsonInvalido = "{ \"aluno\": null, \"evento\": null, "; // JSON incompleto
        mockMvc.perform(post("/participantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }
}
