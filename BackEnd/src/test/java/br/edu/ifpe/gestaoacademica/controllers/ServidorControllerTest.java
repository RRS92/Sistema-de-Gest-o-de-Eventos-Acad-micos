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
public class ServidorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long createdServidorId;

    // Testes de sucesso

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

  //Testes que buscam a falha

    @Test
    @Order(7)
    public void deveFalharBuscarServidorPorIdInexistente() throws Exception {
        mockMvc.perform(get("/servidores/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    public void deveFalharAtualizarServidorInexistente() throws Exception {
        String updateJson = """
            {
                "id": 9999,
                "siape": "000000",
                "cargo": "Inexistente",
                "nome": "Servidor Inexistente",
                "cpf": "00000000000",
                "rg": "00000000",
                "dataNasc": "2000-01-01",
                "telefone": "0000000000",
                "email": "inexistente@example.com",
                "endereco": null,
                "banco": null,
                "utilizador": null
            }
        """;
        mockMvc.perform(put("/servidores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    public void deveFalharInativarServidorInexistente() throws Exception {
        mockMvc.perform(delete("/servidores/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(10)
    public void deveFalharDeletarServidorInexistente() throws Exception {
        mockMvc.perform(delete("/servidores/deletar/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(11)
    public void deveFalharCriarServidorComJsonInvalido() throws Exception {
        // Envia JSON mal formado
        String jsonInvalido = "{ \"siape\": \"123456\", \"cargo\": \"Professor\", ";
        mockMvc.perform(post("/servidores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }
}
