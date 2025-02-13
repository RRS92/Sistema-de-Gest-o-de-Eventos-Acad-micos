package br.edu.ifpe.gestaoacademica.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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
public class ServidorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long createdServidorId;

    @Test
    @Order(1)
    public void deveCriarServidorComSucesso() throws Exception {
        String servidorJson = """
            {
                "siape": "123456",
                "cargo": "Professor",
                "nome": "Carlos Silva",
                "cpf": "12312312312",
                "rg": "12345678",
                "dataNasc": "1980-01-01",
                "telefone": "11912345678",
                "email": "carlos@example.com",
                "endereco": null,
                "banco": null,
                "utilizador": null
            }
        """;

        String response = mockMvc.perform(post("/servidores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(servidorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        createdServidorId = objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @Order(2)
    public void deveListarServidores() throws Exception {
        mockMvc.perform(get("/servidores"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void deveBuscarServidorPorId() throws Exception {
        mockMvc.perform(get("/servidores/" + createdServidorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdServidorId));
    }

    @Test
    @Order(4)
    public void deveAtualizarServidor() throws Exception {
        String updateJson = """
            {
                "id": %d,
                "siape": "654321",
                "cargo": "Coordenador",
                "nome": "Carlos Atualizado",
                "cpf": "12312312312",
                "rg": "12345678",
                "dataNasc": "1980-01-01",
                "telefone": "11987654321",
                "email": "carlos_atualizado@example.com",
                "endereco": null,
                "banco": null,
                "utilizador": null
            }
        """.formatted(createdServidorId);

        mockMvc.perform(put("/servidores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Atualizado"))
                .andExpect(jsonPath("$.telefone").value("11987654321"));
    }

    @Test
    @Order(5)
    public void deveInativarServidor() throws Exception {
        mockMvc.perform(delete("/servidores/" + createdServidorId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    public void deveDeletarServidor() throws Exception {
        mockMvc.perform(delete("/servidores/deletar/" + createdServidorId))
                .andExpect(status().isNoContent());
    }
}
