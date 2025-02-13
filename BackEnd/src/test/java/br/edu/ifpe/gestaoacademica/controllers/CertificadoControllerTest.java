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
public class CertificadoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private static Long createdCertificadoId;
    // Para este teste, assumimos que já existe um Evento com esse id 
    private final Long dummyEventoId = 5L;

    @Test
    @Order(1)
    public void deveCriarCertificadoComSucesso() throws Exception {
        String certificadoJson = "{"
            + "\"cargaHoraria\": \"40\","
            + "\"descricao\": \"Certificado de participação\","
            + "\"idEvento\": " + dummyEventoId
            + "}";
        String response = mockMvc.perform(post("/certificados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(certificadoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        createdCertificadoId = objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @Order(2)
    public void deveListarCertificados() throws Exception {
        mockMvc.perform(get("/certificados"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void deveListarCertificadosPorEvento() throws Exception {
        mockMvc.perform(get("/certificados/evento")
                .param("eventoId", dummyEventoId.toString()))
            .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void deveAtualizarCertificado() throws Exception {
        String updateJson = "{"
            + "\"id\": " + createdCertificadoId + ","
            + "\"cargaHoraria\": \"50\","
            + "\"descricao\": \"Certificado Atualizado\","
            + "\"idEvento\": " + dummyEventoId
            + "}";
        mockMvc.perform(put("/certificados")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.descricao").value("Certificado Atualizado"));
    }

    @Test
    @Order(5)
    public void deveInativarCertificado() throws Exception {
        mockMvc.perform(delete("/certificados/" + createdCertificadoId))
            .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    public void deveDeletarCertificado() throws Exception {
        // Cria um novo certificado e em seguida deleta-o
        String certificadoJson = "{"
            + "\"cargaHoraria\": \"30\","
            + "\"descricao\": \"Certificado para deletar\","
            + "\"idEvento\": " + dummyEventoId
            + "}";
        String response = mockMvc.perform(post("/certificados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(certificadoJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long certId = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(delete("/certificados/deletar/" + certId))
            .andExpect(status().isNoContent());
    }
}
