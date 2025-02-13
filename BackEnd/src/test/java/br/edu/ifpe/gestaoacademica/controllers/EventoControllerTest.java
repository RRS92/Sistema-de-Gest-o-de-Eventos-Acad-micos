package br.edu.ifpe.gestaoacademica.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class EventoControllerTest {

    @Autowired 
    private MockMvc mockMvc;
    
    @Autowired 
    private ObjectMapper objectMapper;
    
    private static Long createdEventoId;

    @Test
    @Order(1)
    public void deveCriarEvento() throws Exception {
        String eventoJson = "{"
            + "\"nome\": \"Evento Teste\","
            + "\"descricao\": \"Descrição do Evento\","
            + "\"data\": \"2025-01-01\","
            + "\"local\": \"Local Teste\","
            + "\"tipo\": \"Tipo Teste\","
            + "\"servidor\": null"
            + "}";
        String response = mockMvc.perform(post("/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        createdEventoId = objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @Order(2)
    public void deveListarEventoPorId() throws Exception {
        mockMvc.perform(get("/eventos/" + createdEventoId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdEventoId));
    }

    @Test
    @Order(3)
    public void deveAtualizarEvento() throws Exception {
        String updateJson = "{"
            + "\"id\": " + createdEventoId + ","
            + "\"nome\": \"Evento Atualizado\","
            + "\"descricao\": \"Descrição Atualizada\","
            + "\"data\": \"2025-02-02\","
            + "\"local\": \"Local Atualizado\","
            + "\"tipo\": \"Tipo Atualizado\","
            + "\"servidor\": null"
            + "}";
        mockMvc.perform(put("/eventos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Evento Atualizado"));
    }

    @Test
    @Order(4)
    public void deveInativarEvento() throws Exception {
        mockMvc.perform(delete("/eventos/" + createdEventoId))
            .andExpect(status().isNoContent());
    }

    @Test
    @Order(5)
    public void deveDeletarEvento() throws Exception {
        // Cria um novo evento e em seguida deleta-o
        String eventoJson = "{"
            + "\"nome\": \"Evento Deletar\","
            + "\"descricao\": \"Evento a ser deletado\","
            + "\"data\": \"2025-03-03\","
            + "\"local\": \"Local Deletar\","
            + "\"tipo\": \"Tipo Deletar\","
            + "\"servidor\": null"
            + "}";
        String response = mockMvc.perform(post("/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventoJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long eventoId = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(delete("/eventos/deletar/" + eventoId))
            .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    public void deveFalharCriarEventoSemNome() throws Exception {
        // Campo 'nome' é obrigatório; omiti-lo deve resultar em Bad Request.
        String eventoJson = "{"
            + "\"descricao\": \"Evento sem nome\","
            + "\"data\": \"2025-01-01\","
            + "\"local\": \"Local Teste\","
            + "\"tipo\": \"Tipo Teste\","
            + "\"servidor\": null"
            + "}";
        mockMvc.perform(post("/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventoJson))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @Order(7)
    public void deveFalharAtualizarEventoInexistente() throws Exception {
        String updateJson = "{"
            + "\"id\": 9999,"  // ID inexistente
            + "\"nome\": \"Evento Inexistente\","
            + "\"descricao\": \"Não existe\","
            + "\"data\": \"2025-04-04\","
            + "\"local\": \"Local Inexistente\","
            + "\"tipo\": \"Tipo Inexistente\","
            + "\"servidor\": null"
            + "}";
        mockMvc.perform(put("/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(8)
    public void deveFalharBuscarEventoPorIdInexistente() throws Exception {
        mockMvc.perform(get("/eventos/9999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(9)
    public void deveFalharInativarEventoInexistente() throws Exception {
        mockMvc.perform(delete("/eventos/9999"))
                .andExpect(status().isNotFound());
    }
    
}
