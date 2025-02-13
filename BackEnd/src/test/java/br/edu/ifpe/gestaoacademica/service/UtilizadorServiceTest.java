package br.edu.ifpe.gestaoacademica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.edu.ifpe.gestaoacademica.controllers.dto.UtilizadorDTO;
import br.edu.ifpe.gestaoacademica.entities.Utilizador;
import br.edu.ifpe.gestaoacademica.entities.enums.AcessLevel;
import br.edu.ifpe.gestaoacademica.repository.UtilizadorRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class UtilizadorServiceTest {

    @Mock
    private UtilizadorRepository utilizadorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UtilizadorService utilizadorService;

    private Utilizador utilizador;
    private UtilizadorDTO utilizadorDTO;

    @BeforeEach
    void setUp() {
        utilizador = new Utilizador();
        utilizador.setId(1L);
        utilizador.setLogin("usuarioTeste");
        utilizador.setSenha("senhaCodificada");
        utilizador.setAtivo(true);
        utilizador.setAcessLevel(AcessLevel.ALUNO);

        utilizadorDTO = new UtilizadorDTO("usuarioTeste", "senha123", null);
    }

    @Test
    void testLoadUserByUsername_ComSucesso() {
        when(utilizadorRepository.findByLogin("usuarioTeste")).thenReturn(utilizador);
        
        UserDetails user = utilizadorService.loadUserByUsername("usuarioTeste");
        
        assertNotNull(user);
        assertEquals("usuarioTeste", user.getUsername());
    }

    @Test
    void testLoadUserByUsername_ComUsuarioNaoEncontrado_DeveLancarExcecao() {
    	when(utilizadorRepository.findByLogin("invalido")).thenThrow(new UsernameNotFoundException("Usuário não encontrado"));
        
        assertThrows(UsernameNotFoundException.class, () -> utilizadorService.loadUserByUsername("invalido"));
    }

    @Test
    void testCadastrarUtilizadorAluno_ComSucesso() {
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCodificada");
        when(utilizadorRepository.save(any(Utilizador.class))).thenReturn(utilizador);

        Utilizador resultado = utilizadorService.cadastrarUtilizadorAluno(utilizadorDTO);

        assertNotNull(resultado);
        assertEquals(AcessLevel.ALUNO, resultado.getAcessLevel());
        verify(utilizadorRepository).save(any(Utilizador.class));
    }

    @Test
    void testCadastrarUtilizadorServidor_ComSucesso() {
        // Simula a criptografia da senha
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCodificada");

        // Configuração do mock para simular a persistência do usuário
        Utilizador utilizadorSalvo = new Utilizador();
        utilizadorSalvo.setId(1L);
        utilizadorSalvo.setLogin("usuario_teste");
        utilizadorSalvo.setSenha("senhaCodificada");
        utilizadorSalvo.setAtivo(true);
        utilizadorSalvo.setAcessLevel(AcessLevel.SERVIDOR);

        when(utilizadorRepository.save(any(Utilizador.class))).thenReturn(utilizadorSalvo);

        // Criando DTO para cadastrar o utilizador
        UtilizadorDTO utilizadorDTO = new UtilizadorDTO("usuario_teste", "senha123", null);

        // Execução do serviço
        Utilizador resultado = utilizadorService.cadastrarUtilizadorServidor(utilizadorDTO);

        // Validações
        assertNotNull(resultado);
        assertEquals(AcessLevel.SERVIDOR, resultado.getAcessLevel());
        assertEquals("senhaCodificada", resultado.getSenha());
        assertTrue(resultado.isAtivo());

        // Verifica se o repositório foi chamado corretamente
        verify(utilizadorRepository).save(any(Utilizador.class));
    }


    @Test
    void testListarUtilizadorPorId_ComSucesso() {
        when(utilizadorRepository.findById(1L)).thenReturn(Optional.of(utilizador));
        
        Optional<Utilizador> resultado = utilizadorService.listarUtilizadorPorId(1L);
        
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void testListarUtilizador() {
        when(utilizadorRepository.findAllByAtivoTrue()).thenReturn(Arrays.asList(utilizador));
        
        List<Utilizador> lista = utilizadorService.listarUtilizador();
        
        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
    }

    @Test
    void testAtualizarUtilizador_ComSucesso() {
        when(utilizadorRepository.findById(1L)).thenReturn(Optional.of(utilizador));
        when(passwordEncoder.encode("novaSenha")).thenReturn("novaSenhaCodificada");
        when(utilizadorRepository.save(any(Utilizador.class))).thenReturn(utilizador);

        UtilizadorDTO dtoAtualizado = new UtilizadorDTO(1L, "novoLogin", "novaSenha", null);
        Utilizador atualizado = utilizadorService.atualizarUtilizador(dtoAtualizado);

        assertNotNull(atualizado);
        assertEquals("novoLogin", atualizado.getLogin());
        assertEquals("novaSenhaCodificada", atualizado.getSenha());
        assertEquals(AcessLevel.ALUNO, atualizado.getAcessLevel()); // Certifica que não mudou
        verify(utilizadorRepository).save(any(Utilizador.class));
    }


    @Test
    void testAtualizarUtilizador_ComIdNaoEncontrado_DeveLancarExcecao() {
        when(utilizadorRepository.findById(99L)).thenReturn(Optional.empty());

        UtilizadorDTO dtoInvalido = new UtilizadorDTO(99L, "novoLogin", "novaSenha", null);

        assertThrows(EntityNotFoundException.class, () -> utilizadorService.atualizarUtilizador(dtoInvalido));
    }

    @Test
    void testInativarUtilizador_ComSucesso() {
        when(utilizadorRepository.getReferenceById(1L)).thenReturn(utilizador);

        utilizadorService.inativarUtilizador(1L);

        assertFalse(utilizador.isAtivo());
        verify(utilizadorRepository).save(utilizador);
        verify(utilizadorRepository, times(1)).getReferenceById(1L);
    }


    @Test
    void testDeletarUtilizador() {
        when(utilizadorRepository.existsById(1L)).thenReturn(true); // Simula que o ID existe
        doNothing().when(utilizadorRepository).deleteById(1L);

        utilizadorService.deletarUtilizador(1L);

        verify(utilizadorRepository, times(1)).deleteById(1L);
    }

    
    @Test
    void testDeletarUtilizador_ComIdNaoExistente_DeveLancarExcecao() {
        when(utilizadorRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> utilizadorService.deletarUtilizador(99L));
        verify(utilizadorRepository, times(0)).deleteById(99L);
    }
}
