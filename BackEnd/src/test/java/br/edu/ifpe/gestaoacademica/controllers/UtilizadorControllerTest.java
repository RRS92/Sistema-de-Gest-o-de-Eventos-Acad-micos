package br.edu.ifpe.gestaoacademica.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(OrderAnnotation.class)
public class UtilizadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long createdUtilizadorId;

    @Test
    @Order(1)
    public void deveCriarUtilizador() throws Exception {
        String utilizadorJson = "{"
                + "\"login\": \"usuario1\","
                + "\"senha\": \"senha123\","
                + "\"fotoBase64\": \"\""
                + "}";

        String response = mockMvc.perform(post("/login/cadastrarAluno")
                .contentType(MediaType.APPLICATION_JSON)
                .content(utilizadorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        createdUtilizadorId = objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @Order(2)
    public void deveListarUtilizadores() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void deveListarUtilizadorPorId() throws Exception {
        mockMvc.perform(get("/login/" + createdUtilizadorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUtilizadorId));
    }

    @Test
    @Order(4)
    public void deveDeletarUtilizador() throws Exception {
        mockMvc.perform(delete("/login/deletar/" + createdUtilizadorId))
                .andExpect(status().isNoContent());
    }
}