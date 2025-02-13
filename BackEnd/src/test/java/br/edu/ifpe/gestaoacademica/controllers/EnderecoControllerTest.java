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
public class EnderecoControllerTest {

    @Autowired 
    private MockMvc mockMvc;
    
    @Autowired 
    private ObjectMapper objectMapper;
    
    private static Long createdEnderecoId;
    
    @Test
    @Order(1)
    public void deveCriarEnderecoComSucesso() throws Exception {
        String enderecoJson = "{"
            + "\"rua\": \"Rua Teste\","
            + "\"numero\": \"123\","
            + "\"bairro\": \"Bairro Teste\","
            + "\"cidade\": \"Cidade Teste\","
            + "\"estado\": \"Estado Teste\","
            + "\"cep\": \"12345000\","
            + "\"complemento\": \"Complemento Teste\""
            + "}";
        String response = mockMvc.perform(post("/enderecos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(enderecoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        createdEnderecoId = objectMapper.readTree(response).get("id").asLong();
    }
    
    @Test
    @Order(2)
    public void deveListarEnderecoPorId() throws Exception {
        mockMvc.perform(get("/enderecos/" + createdEnderecoId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdEnderecoId));
    }
    
    @Test
    @Order(3)
    public void deveListarEnderecos() throws Exception {
        mockMvc.perform(get("/enderecos"))
            .andExpect(status().isOk());
    }
    
    @Test
    @Order(4)
    public void deveAtualizarEndereco() throws Exception {
        String updateJson = "{"
            + "\"id\": " + createdEnderecoId + ","
            + "\"rua\": \"Rua Atualizada\","
            + "\"numero\": \"456\","
            + "\"bairro\": \"Bairro Atualizado\","
            + "\"cidade\": \"Cidade Atualizada\","
            + "\"estado\": \"Estado Atualizado\","
            + "\"cep\": \"54321000\","
            + "\"complemento\": \"Complemento Atualizado\""
            + "}";
        mockMvc.perform(put("/enderecos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rua").value("Rua Atualizada"));
    }
    
    @Test
    @Order(5)
    public void deveInativarEndereco() throws Exception {
        mockMvc.perform(delete("/enderecos/" + createdEnderecoId))
            .andExpect(status().isNoContent());
    }
    
    @Test
    @Order(6)
    public void deveDeletarEndereco() throws Exception {
        // Cria um novo endereço e em seguida deleta-o
        String enderecoJson = "{"
            + "\"rua\": \"Rua Deletar\","
            + "\"numero\": \"789\","
            + "\"bairro\": \"Bairro Deletar\","
            + "\"cidade\": \"Cidade Deletar\","
            + "\"estado\": \"Estado Deletar\","
            + "\"cep\": \"11111111\","
            + "\"complemento\": \"Complemento Deletar\""
            + "}";
        String response = mockMvc.perform(post("/enderecos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(enderecoJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long enderecoId = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(delete("/enderecos/deletar/" + enderecoId))
            .andExpect(status().isNoContent());
    }
    
    @Test
    @Order(7)
    public void deveFalharCriarEnderecoSemRua() throws Exception {
        // O campo "rua" é obrigatório. Omiti-lo deve resultar em Bad Request (400)
        String enderecoJson = "{"
            + "\"numero\": \"123\","
            + "\"bairro\": \"Bairro Teste\","
            + "\"cidade\": \"Cidade Teste\","
            + "\"estado\": \"Estado Teste\","
            + "\"cep\": \"12345000\","
            + "\"complemento\": \"Complemento Teste\""
            + "}";
        mockMvc.perform(post("/enderecos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(enderecoJson))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @Order(8)
    public void deveFalharAtualizarEnderecoInexistente() throws Exception {
        // Tenta atualizar um endereço com ID inexistente (9999)
        String updateJson = "{"
            + "\"id\": 9999,"
            + "\"rua\": \"Rua Inexistente\","
            + "\"numero\": \"000\","
            + "\"bairro\": \"Bairro Inexistente\","
            + "\"cidade\": \"Cidade Inexistente\","
            + "\"estado\": \"Estado Inexistente\","
            + "\"cep\": \"00000000\","
            + "\"complemento\": \"Sem Complemento\""
            + "}";
        mockMvc.perform(put("/enderecos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(9)
    public void deveFalharBuscarEnderecoPorIdInexistente() throws Exception {
        // Tenta buscar um endereço com ID inexistente (9999)
        mockMvc.perform(get("/enderecos/9999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(10)
    public void deveFalharInativarEnderecoInexistente() throws Exception {
        // Tenta inativar um endereço com ID inexistente (9999)
        mockMvc.perform(delete("/enderecos/9999"))
                .andExpect(status().isNotFound());
    }
    
}
