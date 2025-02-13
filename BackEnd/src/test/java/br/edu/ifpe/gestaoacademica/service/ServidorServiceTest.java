package br.edu.ifpe.gestaoacademica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
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
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.ifpe.gestaoacademica.controllers.dto.ServidorDTO;
import br.edu.ifpe.gestaoacademica.entities.Banco;
import br.edu.ifpe.gestaoacademica.entities.Endereco;
import br.edu.ifpe.gestaoacademica.entities.Servidor;
import br.edu.ifpe.gestaoacademica.entities.Utilizador;
import br.edu.ifpe.gestaoacademica.repository.ServidorRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ServidorServiceTest {

    @Mock
    private ServidorRepository servidorRepository;

    @InjectMocks
    private ServidorService servidorService;

    private Servidor servidor;
    private ServidorDTO servidorDTO;
    
    private Endereco endereco;
    private Banco banco;
    private Utilizador utilizador;

    @BeforeEach
    void setUp() {
        servidor = new Servidor();
        servidor.setId(1L);
        servidor.setNome("Teste");
        servidor.setSiape("123456");
        servidor.setAtivo(true);

        servidorDTO = new ServidorDTO(1L, "123456", "Professor", "Teste", 
        		"12345678900", "RG12345", "20/09/2001", "email@test.com", 
        		"999999999", endereco, banco, utilizador);
    }

    @Test
    void cadastrarServidor_DeveSalvarServidor() {
        when(servidorRepository.save(any(Servidor.class))).thenReturn(servidor);

        Servidor resultado = servidorService.cadastrarServidor(servidorDTO);

        assertNotNull(resultado);
        assertEquals("Teste", resultado.getNome());
        verify(servidorRepository, times(1)).save(any(Servidor.class));
    }

    @Test
    void listarServidores_DeveRetornarLista() {
        when(servidorRepository.findAllByAtivoTrue()).thenReturn(List.of(servidor));

        List<Servidor> resultado = servidorService.listarServidores();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(servidorRepository, times(1)).findAllByAtivoTrue();
    }

    @Test
    void listarServidor_DeveRetornarServidorSeEncontrado() {
        when(servidorRepository.findById(1L)).thenReturn(Optional.of(servidor));

        Optional<Servidor> resultado = servidorService.listarServidor(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Teste", resultado.get().getNome());
        verify(servidorRepository, times(1)).findById(1L);
    }

    @Test
    void listarServidor_DeveRetornarVazioSeNaoEncontrado() {
        when(servidorRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Servidor> resultado = servidorService.listarServidor(1L);

        assertTrue(resultado.isEmpty());
        verify(servidorRepository, times(1)).findById(1L);
    }

    @Test
    void atualizarServidor_DeveAtualizarDados() {
        when(servidorRepository.findById(1L)).thenReturn(Optional.of(servidor));
        when(servidorRepository.save(any(Servidor.class))).thenReturn(servidor);

        ServidorDTO dtoAtualizado = new ServidorDTO(1L, "654321", "Professor", "Novo Nome", 
                "12345678900", "RG12345", "20/09/2001", "email@test.com", 
                "999999999", endereco, banco, utilizador);

        Servidor atualizado = servidorService.atualizarServidor(dtoAtualizado);

        assertNotNull(atualizado);
        assertEquals("Novo Nome", atualizado.getNome());
        assertEquals("654321", atualizado.getSiape());

        verify(servidorRepository, times(1)).findById(1L);
        verify(servidorRepository, times(1)).save(any(Servidor.class));
    }

    @Test
    void atualizarServidor_DeveLancarExcecaoSeNaoEncontrado() {
        when(servidorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> servidorService.atualizarServidor(servidorDTO));

        verify(servidorRepository, times(1)).findById(1L);
        verify(servidorRepository, never()).save(any(Servidor.class));
    }

    @Test
    void inativarServidor_DeveAlterarStatusParaInativo() {
        when(servidorRepository.getReferenceById(1L)).thenReturn(servidor);

        servidorService.inativarServidor(1L);

        assertFalse(servidor.isAtivo());
        verify(servidorRepository, times(1)).getReferenceById(1L);
        verify(servidorRepository, times(1)).save(servidor);
    }

    @Test
    void deletarServidor_DeveRemoverServidor() {
        doNothing().when(servidorRepository).deleteById(1L);

        servidorService.deletarServidor(1L);

        verify(servidorRepository, times(1)).deleteById(1L);
    }

    @Test
    void cadastrarServidor_DeveLancarExcecaoSeDTONulo() {
        assertThrows(IllegalArgumentException.class, () -> servidorService.cadastrarServidor(null));
    }

    @Test
    void cadastrarServidor_DeveLancarExcecaoSeDadosInvalidos() {
        ServidorDTO dtoInvalido = new ServidorDTO(null, "", "", "", "", "", "", "", "", null, null, null);
        
        assertThrows(IllegalArgumentException.class, () -> servidorService.cadastrarServidor(dtoInvalido));
    }

    @Test
    void inativarServidor_DeveLancarExcecaoSeNaoEncontrado() {
        when(servidorRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> servidorService.inativarServidor(1L));

        verify(servidorRepository, times(1)).getReferenceById(1L);
        verify(servidorRepository, never()).save(any(Servidor.class));
    }
}
