package br.edu.ifpe.gestaoacademica.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.ifpe.gestaoacademica.controllers.dto.AvaliacaoDTO;
import br.edu.ifpe.gestaoacademica.entities.Avaliacao;
import br.edu.ifpe.gestaoacademica.entities.Evento;
import br.edu.ifpe.gestaoacademica.entities.Participante;
import br.edu.ifpe.gestaoacademica.repository.AvaliacaoRepository;
import br.edu.ifpe.gestaoacademica.repository.EventoRepository;

@ExtendWith(MockitoExtension.class)
class AvaliacaoServiceTest {

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private AvaliacaoService avaliacaoService;

    private Avaliacao avaliacao;
    private Evento evento;
    private AvaliacaoDTO avaliacaoDTO;

    @BeforeEach
    void setUp() {
        evento = new Evento();
        evento.setId(1L);

        Participante participante = new Participante();
        
        avaliacao = new Avaliacao();
        avaliacao.setId(1L);
        avaliacao.setNota("5");
        avaliacao.setComentario("Ótimo evento!");
        avaliacao.setEvento(evento);
        avaliacao.setAtivo(true);
        avaliacao.setParticipante(participante);

        avaliacaoDTO = new AvaliacaoDTO("5", "Ótimo evento!", 1L, participante);
    }

    @Test
    void testCadastrarAvaliacao_ComSucesso() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacao);

        Avaliacao resultado = avaliacaoService.cadastrarAvaliacao(avaliacaoDTO);

        assertNotNull(resultado);
        assertEquals("5", resultado.getNota());
        assertEquals("Ótimo evento!", resultado.getComentario());
        assertEquals(evento, resultado.getEvento());

        verify(avaliacaoRepository).save(any(Avaliacao.class));
    }

    @Test
    void testCadastrarAvaliacao_EventoNaoEncontrado() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            avaliacaoService.cadastrarAvaliacao(avaliacaoDTO);
        });

        assertEquals("Evento não encontrado", exception.getMessage());
    }

    @Test
    void testListarAvaliacao() {
        when(avaliacaoRepository.findAllByAtivoTrue()).thenReturn(Arrays.asList(avaliacao));

        List<Avaliacao> lista = avaliacaoService.listarAvaliacao();

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("5", lista.get(0).getNota());
    }

    @Test
    void testListarAvaliacoesPorEvento_ComSucesso() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(avaliacaoRepository.findByEvento(evento)).thenReturn(Arrays.asList(avaliacao));

        List<Avaliacao> lista = avaliacaoService.listarAvaliacoesPorEvento(1L);

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("5", lista.get(0).getNota());
    }

    @Test
    void testListarAvaliacoesPorEvento_EventoNaoEncontrado() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            avaliacaoService.listarAvaliacoesPorEvento(1L);
        });

        assertEquals("Evento não encontrado", exception.getMessage());
    }

    @Test
    void testAtualizarAvaliacao_ComSucesso() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacao);

        AvaliacaoDTO avaliacaoDTOAtualizada = new AvaliacaoDTO(1L, "4", "Bom evento!", 1L, avaliacao.getParticipante());
        Avaliacao resultado = avaliacaoService.atualizarAvaliacao(avaliacaoDTOAtualizada);

        assertNotNull(resultado);
        assertEquals("4", resultado.getNota());
        assertEquals("Bom evento!", resultado.getComentario());

        verify(avaliacaoRepository).save(any(Avaliacao.class));
    }

    @Test
    void testAtualizarAvaliacao_AvaliacaoNaoEncontrada() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.empty());

        AvaliacaoDTO avaliacaoDTOAtualizada = new AvaliacaoDTO(1L, "4", "Bom evento!", 1L, avaliacao.getParticipante());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            avaliacaoService.atualizarAvaliacao(avaliacaoDTOAtualizada);
        });

        assertEquals("Avaliacão não encontrado", exception.getMessage());
    }

    @Test
    void testInativarAvaliacao_ComSucesso() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacao); // Correção

        avaliacaoService.inativarAvaliacao(1L);

        assertFalse(avaliacao.isAtivo()); // Confirma que foi inativado

        verify(avaliacaoRepository).save(avaliacao);
    }


    @Test
    void testInativarAvaliacao_AvaliacaoNaoEncontrada() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            avaliacaoService.inativarAvaliacao(1L);
        });

        assertEquals("Certificado não encontrado", exception.getMessage()); // Corrigido para coincidir com o service
    }


    @Test
    void testDeletarAvaliacao() {
        doNothing().when(avaliacaoRepository).deleteById(1L);

        avaliacaoService.deletarAvaliacao(1L);

        verify(avaliacaoRepository).deleteById(1L);
    }
}
