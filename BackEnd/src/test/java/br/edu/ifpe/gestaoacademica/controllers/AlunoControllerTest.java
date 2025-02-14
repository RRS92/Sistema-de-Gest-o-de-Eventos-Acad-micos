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

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long createdAlunoId;

  //Testes que buscam o sucesso

    @Test
    @Order(1)
    public void deveCriarAlunoComSucesso() throws Exception {
        String alunoJson = """
            {
                "matricula": "2021001",
                "nome": "João Silva",
                "cpf": "12345678900",
                "rg": "1234567",
                "dataNasc": "1990-01-01",
                "telefone": "1112345678",
                "email": "joao@example.com",
                "endereco": null,
                "banco": null,
                "participante": [],
                "utilizador": null
            }
        """;

        String response = mockMvc.perform(post("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(alunoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        createdAlunoId = objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @Order(2)
    public void deveListarAlunos() throws Exception {
        mockMvc.perform(get("/alunos"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void deveBuscarAlunoPorId() throws Exception {
        mockMvc.perform(get("/alunos/" + createdAlunoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdAlunoId));
    }

    @Test
    @Order(4)
    public void deveAtualizarAluno() throws Exception {
        String alunoJson = """
            {
                "id": %d,
                "matricula": "2021001",
                "nome": "João Atualizado",
                "cpf": "12345678900",
                "rg": "1234567",
                "dataNasc": "1990-01-01",
                "telefone": "1112345678",
                "email": "joao_atualizado@example.com",
                "endereco": null,
                "banco": null,
                "participante": [],
                "utilizador": null
            }
        """.formatted(createdAlunoId);

        mockMvc.perform(put("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(alunoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Atualizado"))
                .andExpect(jsonPath("$.email").value("joao_atualizado@example.com"));
    }

    @Test
    @Order(5)
    public void deveInativarAluno() throws Exception {
        mockMvc.perform(delete("/alunos/" + createdAlunoId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    public void deveDeletarAluno() throws Exception {
        mockMvc.perform(delete("/alunos/deletar/" + createdAlunoId))
                .andExpect(status().isNoContent());
    }
    
  //Testes que buscam a falha

    @Test
    @Order(7)
    public void deveFalharCriarAlunoSemNome() throws Exception {
        // O campo "nome" é obrigatório. Omiti-lo deve retornar 400 Bad Request.
        String alunoJson = """
            {
                "matricula": "2021002",
                "nome": "",
                "cpf": "12345678900",
                "rg": "1234567",
                "dataNasc": "1990-01-01",
                "telefone": "1112345678",
                "email": "semnome@example.com",
                "endereco": null,
                "banco": null,
                "participante": [],
                "utilizador": null
            }
        """;
        mockMvc.perform(post("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(alunoJson))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @Order(8)
    public void deveFalharBuscarAlunoPorIdInexistente() throws Exception {
        mockMvc.perform(get("/alunos/9999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(9)
    public void deveFalharAtualizarAlunoInexistente() throws Exception {
        String alunoJson = """
            {
                "id": 9999,
                "matricula": "2021003",
                "nome": "Aluno Inexistente",
                "cpf": "12345678900",
                "rg": "1234567",
                "dataNasc": "1990-01-01",
                "telefone": "1112345678",
                "email": "inexistente@example.com",
                "endereco": null,
                "banco": null,
                "participante": [],
                "utilizador": null
            }
        """;
        mockMvc.perform(put("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(alunoJson))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(10)
    public void deveFalharInativarAlunoInexistente() throws Exception {
        mockMvc.perform(delete("/alunos/9999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(11)
    public void deveFalharDeletarAlunoInexistente() throws Exception {
        mockMvc.perform(delete("/alunos/deletar/9999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @Order(12)
    public void deveFalharCriarAlunoComJsonInvalido() throws Exception {
        // JSON mal formado
        String jsonInvalido = "{ \"matricula\": \"2021004\", \"nome\": \"João\", ";
        mockMvc.perform(post("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }
}
