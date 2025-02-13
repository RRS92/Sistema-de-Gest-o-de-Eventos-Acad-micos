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
public class BancoControllerTest {

    @Autowired 
    private MockMvc mockMvc;
    
    @Autowired 
    private ObjectMapper objectMapper;
    
    private static Long createdBancoId;
    
    @Test
    @Order(1)
    public void deveCriarBancoComSucesso() throws Exception {
        String bancoJson = "{"
            + "\"nomeBanco\": \"Banco X\","
            + "\"numConta\": \"123456\","
            + "\"agencia\": \"001\","
            + "\"operacao\": \"001\""
            + "}";
        String response = mockMvc.perform(post("/bancos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bancoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        createdBancoId = objectMapper.readTree(response).get("id").asLong();
    }
    
    @Test
    @Order(2)
    public void deveListarBancoPorID() throws Exception {
        mockMvc.perform(get("/bancos/" + createdBancoId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdBancoId));
    }
    
    @Test
    @Order(3)
    public void deveListarBanco() throws Exception {
        mockMvc.perform(get("/bancos"))
            .andExpect(status().isOk());
    }
    
    @Test
    @Order(4)
    public void deveAtualizarBanco() throws Exception {
        String updateJson = "{"
            + "\"id\": " + createdBancoId + ","
            + "\"nomeBanco\": \"Banco Atualizado\","
            + "\"numConta\": \"654321\","
            + "\"agencia\": \"002\","
            + "\"operacao\": \"002\""
            + "}";
        mockMvc.perform(put("/bancos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nomeBanco").value("Banco Atualizado"));
    }
    
    @Test
    @Order(5)
    public void deveInativarBanco() throws Exception {
        mockMvc.perform(delete("/bancos/" + createdBancoId))
            .andExpect(status().isNoContent());
    }
    
    @Test
    @Order(6)
    public void deveDeletarBanco() throws Exception {
        // Cria um novo banco e em seguida deleta-o
        String bancoJson = "{"
            + "\"nomeBanco\": \"Banco Y\","
            + "\"numConta\": \"111222\","
            + "\"agencia\": \"003\","
            + "\"operacao\": \"003\""
            + "}";
        String response = mockMvc.perform(post("/bancos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bancoJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long bancoId = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(delete("/bancos/deletar/" + bancoId))
            .andExpect(status().isNoContent());
    }
    
    @Test
    @Order(7)
    public void deveFalharCriarBancoSemNomeBanco() throws Exception {
        // O campo nomeBanco é obrigatório; omitir esse campo deve resultar em Bad Request
        String bancoJson = "{"
            + "\"numConta\": \"999999\","
            + "\"agencia\": \"004\","
            + "\"operacao\": \"004\""
            + "}";
        mockMvc.perform(post("/bancos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bancoJson))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @Order(8)
    public void deveFalharAtualizarBancoInexistente() throws Exception {
        String updateJson = "{"
            + "\"id\": 9999,"  // ID inexistente
            + "\"nomeBanco\": \"Banco Inexistente\","
            + "\"numConta\": \"000000\","
            + "\"agencia\": \"000\","
            + "\"operacao\": \"000\""
            + "}";
        mockMvc.perform(put("/bancos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(9)
    public void deveFalharBuscarBancoPorIDInexistente() throws Exception {
        mockMvc.perform(get("/bancos/9999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(10)
    public void deveFalharInativarBancoInexistente() throws Exception {
        mockMvc.perform(delete("/bancos/9999"))
                .andExpect(status().isNotFound());
    }
    
}
