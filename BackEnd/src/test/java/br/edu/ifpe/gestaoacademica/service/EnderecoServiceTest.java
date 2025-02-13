package br.edu.ifpe.gestaoacademica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.ifpe.gestaoacademica.controllers.dto.EnderecoDTO;
import br.edu.ifpe.gestaoacademica.entities.Endereco;
import br.edu.ifpe.gestaoacademica.repository.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class EnderecoServiceTest {
	@Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    private EnderecoDTO enderecoDTO;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        enderecoDTO = new EnderecoDTO(1L, "Rua Teste", "123", "Bairro Teste", "Cidade Teste", "Estado Teste", "12345678", "Complemento Teste");
        endereco = new Endereco(1L, "Rua Teste", "123", "Bairro Teste", "Cidade Teste", "Estado Teste", "12345678", "Complemento Teste", true, null);
    }

    @Test
    void testCadastrarEndereco() {
        when(enderecoRepository.save(Mockito.any(Endereco.class))).thenReturn(endereco);

        Endereco resultado = enderecoService.cadastrarEndereco(enderecoDTO);

        assertNotNull(resultado);
        assertEquals("Rua Teste", resultado.getRua());
        verify(enderecoRepository, times(1)).save(Mockito.any(Endereco.class));
    }

    @Test
    void testAtualizarEndereco() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));
        when(enderecoRepository.save(Mockito.any(Endereco.class))).thenReturn(endereco);

        Endereco resultado = enderecoService.atualizarEndereco(enderecoDTO);

        assertNotNull(resultado);
        assertEquals("Rua Teste", resultado.getRua());
        verify(enderecoRepository, times(1)).findById(1L);
        verify(enderecoRepository, times(1)).save(Mockito.any(Endereco.class));
    }

    @Test
    void testAtualizarEnderecoEnderecoNaoEncontrado() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> enderecoService.atualizarEndereco(enderecoDTO));

        verify(enderecoRepository, times(1)).findById(1L);
    }

    @Test
    void testListarEnderecoId() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        Optional<Endereco> resultado = enderecoService.listarEnderecoId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Rua Teste", resultado.get().getRua());
        verify(enderecoRepository, times(1)).findById(1L);
    }

    @Test
    void testListarEnderecos() {
        when(enderecoRepository.findAllByAtivoTrue()).thenReturn(List.of(endereco));

        var enderecos = enderecoService.listarEnderecos();

        assertNotNull(enderecos);
        assertFalse(enderecos.isEmpty());
        verify(enderecoRepository, times(1)).findAllByAtivoTrue();
    }

    @Test
    void testInativarEndereco() {
        when(enderecoRepository.getReferenceById(1L)).thenReturn(endereco);
        when(enderecoRepository.save(Mockito.any(Endereco.class))).thenReturn(endereco);

        enderecoService.inativarEndereco(1L);

        verify(enderecoRepository, times(1)).getReferenceById(1L);
        verify(enderecoRepository, times(1)).save(Mockito.any(Endereco.class));
    }

    @Test
    void testDeletarEndereco() {
        doNothing().when(enderecoRepository).deleteById(1L);

        enderecoService.deletarEndereco(1L);

        verify(enderecoRepository, times(1)).deleteById(1L);
    }
}
