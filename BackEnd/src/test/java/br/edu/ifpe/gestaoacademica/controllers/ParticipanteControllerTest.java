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

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private static Long createdParticipanteId;
    
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
        // Supondo que para o evento com id 1 a lista seja retornada
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
}
