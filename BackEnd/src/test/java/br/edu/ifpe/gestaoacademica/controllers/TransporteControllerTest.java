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
public class TransporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long createdTransporteId;
    private final Long dummyEventoId = 1L;  

  //Testes que buscam o sucesso

    @Test
    @Order(1)
    public void deveCriarTransporte() throws Exception {
        String transporteJson = "{"
                + "\"categoria\": \"Ônibus\","
                + "\"placa\": \"XYZ-1234\","
                + "\"quilometragem\": \"10000\","
                + "\"nomeMotorista\": \"Carlos Oliveira\","
                + "\"horaSaida\": \"08:00\","
                + "\"horaChegada\": \"18:00\","
                + "\"idEvento\": " + dummyEventoId + ","
                + "\"servidor\": []"
                + "}";
        String response = mockMvc.perform(post("/transportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transporteJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        createdTransporteId = objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @Order(2)
    public void deveListarTransportes() throws Exception {
        mockMvc.perform(get("/transportes"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void deveAtualizarTransporte() throws Exception {
        String updateJson = "{"
                + "\"id\": " + createdTransporteId + ","
                + "\"categoria\": \"Micro-ônibus\","
                + "\"placa\": \"ABC-5678\","
                + "\"quilometragem\": \"20000\","
                + "\"nomeMotorista\": \"João Santos\","
                + "\"horaSaida\": \"09:00\","
                + "\"horaChegada\": \"17:00\","
                + "\"idEvento\": " + dummyEventoId + ","
                + "\"servidor\": []"
                + "}";
        mockMvc.perform(put("/transportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria").value("Micro-ônibus"))
                .andExpect(jsonPath("$.placa").value("ABC-5678"));
    }

    @Test
    @Order(4)
    public void deveInativarTransporte() throws Exception {
        mockMvc.perform(delete("/transportes/" + createdTransporteId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(5)
    public void deveDeletarTransporte() throws Exception {
        String transporteJson = "{"
                + "\"categoria\": \"Van\","
                + "\"placa\": \"LMN-4567\","
                + "\"quilometragem\": \"5000\","
                + "\"nomeMotorista\": \"Ana Souza\","
                + "\"horaSaida\": \"10:00\","
                + "\"horaChegada\": \"16:00\","
                + "\"idEvento\": " + dummyEventoId + ","
                + "\"servidor\": []"
                + "}";
        String response = mockMvc.perform(post("/transportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transporteJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long transporteId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/transportes/deletar/" + transporteId))
                .andExpect(status().isNoContent());
    }
    
  //Testes que buscam a falha

    @Test
    @Order(6)
    public void deveFalharCriarTransporteSemCategoria() throws Exception {
        // O campo "categoria" é obrigatório. Omiti-lo deve resultar em Bad Request.
        String transporteJson = "{"
                + "\"placa\": \"XYZ-1234\","
                + "\"quilometragem\": \"10000\","
                + "\"nomeMotorista\": \"Carlos Oliveira\","
                + "\"horaSaida\": \"08:00\","
                + "\"horaChegada\": \"18:00\","
                + "\"idEvento\": " + dummyEventoId + ","
                + "\"servidor\": []"
                + "}";
        mockMvc.perform(post("/transportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transporteJson))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @Order(7)
    public void deveFalharAtualizarTransporteInexistente() throws Exception {
        String updateJson = "{"
                + "\"id\": 9999,"  // ID inexistente
                + "\"categoria\": \"Micro-ônibus\","
                + "\"placa\": \"ABC-5678\","
                + "\"quilometragem\": \"20000\","
                + "\"nomeMotorista\": \"João Santos\","
                + "\"horaSaida\": \"09:00\","
                + "\"horaChegada\": \"17:00\","
                + "\"idEvento\": " + dummyEventoId + ","
                + "\"servidor\": []"
                + "}";
        mockMvc.perform(put("/transportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(8)
    public void deveFalharInativarTransporteInexistente() throws Exception {
        mockMvc.perform(delete("/transportes/9999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(9)
    public void deveFalharCriarTransporteComJsonInvalido() throws Exception {
        // Envia JSON malformado
        String jsonInvalido = "{ \"categoria\": \"Ônibus\", \"placa\": "; // JSON incompleto
        mockMvc.perform(post("/transportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }
}
