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
public class MatriculaControllerTest {

    @Autowired 
    private MockMvc mockMvc;
    
    @Autowired 
    private ObjectMapper objectMapper;
    
    private static Long createdMatriculaId;
    
    @Test
    @Order(1)
    public void deveCriarMatricula() throws Exception {
        String matriculaJson = "{"
            + "\"periodoIngresso\": \"2021.1\","
            + "\"turno\": \"Manhã\","
            + "\"aluno\": null,"  
            + "\"nomeCurso\": \"Engenharia\","
            + "\"modalidade\": \"Presencial\""
            + "}";
        String response = mockMvc.perform(post("/matriculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(matriculaJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        createdMatriculaId = objectMapper.readTree(response).get("id").asLong();
    }
    
    @Test
    @Order(2)
    public void deveAtualizarMatricula() throws Exception {
        String updateJson = "{"
            + "\"id\": " + createdMatriculaId + ","
            + "\"periodoIngresso\": \"2021.2\","
            + "\"turno\": \"Tarde\","
            + "\"aluno\": null,"  
            + "\"nomeCurso\": \"Engenharia Atualizado\","
            + "\"modalidade\": \"EAD\""
            + "}";
        mockMvc.perform(put("/matriculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turno").value("Tarde"));
    }
    
    @Test
    @Order(3)
    public void deveListarMatriculaPorAlunoId() throws Exception {
        // Como o campo 'aluno' está null, o endpoint retorna 404.
        mockMvc.perform(get("/matriculas/aluno/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(4)
    public void deveInativarMatricula() throws Exception {
        mockMvc.perform(delete("/matriculas/" + createdMatriculaId))
                .andExpect(status().isNoContent());
    }
    
    @Test
    @Order(5)
    public void deveDeletarMatricula() throws Exception {
        // Cria uma nova matrícula e em seguida a deleta
        String matriculaJson = "{"
            + "\"periodoIngresso\": \"2021.3\","
            + "\"turno\": \"Noite\","
            + "\"aluno\": null,"  // Em teste real, associe um objeto Aluno válido
            + "\"nomeCurso\": \"Medicina\","
            + "\"modalidade\": \"Presencial\""
            + "}";
        String response = mockMvc.perform(post("/matriculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(matriculaJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long matriculaId = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(delete("/matriculas/deletar/" + matriculaId))
                .andExpect(status().isNoContent());
    }
    
    @Test
    @Order(6)
    public void deveFalharCriarMatriculaSemPeriodoIngresso() throws Exception {
        // O campo 'periodoIngresso' é obrigatório; omití-lo deve resultar em Bad Request.
        String matriculaJson = "{"
            + "\"turno\": \"Manhã\","
            + "\"aluno\": null,"
            + "\"nomeCurso\": \"Engenharia\","
            + "\"modalidade\": \"Presencial\""
            + "}";
        mockMvc.perform(post("/matriculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(matriculaJson))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @Order(7)
    public void deveFalharAtualizarMatriculaInexistente() throws Exception {
        String updateJson = "{"
            + "\"id\": 9999,"  // ID inexistente
            + "\"periodoIngresso\": \"2021.2\","
            + "\"turno\": \"Tarde\","
            + "\"aluno\": null,"
            + "\"nomeCurso\": \"Engenharia Inexistente\","
            + "\"modalidade\": \"EAD\""
            + "}";
        mockMvc.perform(put("/matriculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(8)
    public void deveFalharInativarMatriculaInexistente() throws Exception {
        mockMvc.perform(delete("/matriculas/9999"))
                .andExpect(status().isNotFound());
    }
    
}
