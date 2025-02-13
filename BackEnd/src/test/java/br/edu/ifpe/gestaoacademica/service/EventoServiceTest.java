package br.edu.ifpe.gestaoacademica.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.ifpe.gestaoacademica.controllers.dto.EventoDTO;
import br.edu.ifpe.gestaoacademica.entities.Evento;
import br.edu.ifpe.gestaoacademica.entities.Servidor;
import br.edu.ifpe.gestaoacademica.repository.EventoRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private EventoService eventoService;

    private Evento evento;
    private EventoDTO eventoDTO;
    private Servidor servidor;

    @BeforeEach
    void setUp() {
        servidor = new Servidor();
        evento = criarEvento();
        eventoDTO = criarEventoDTO();
    }

    private Evento criarEvento() {
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setNome("Evento Teste");
        evento.setDescricao("Descricao Teste");
        evento.setData("2025-02-12");
        evento.setLocal("Local Teste");
        evento.setTipo("Tipo Teste");
        evento.setAtivo(true);
        evento.setServidor(servidor);
        return evento;
    }

    private EventoDTO criarEventoDTO() {
        return new EventoDTO(1L, "Evento Teste", "Descricao Teste", "2025-02-12", "Local Teste", "Tipo Teste", servidor);
    }

    @Test
    void deveCadastrarEvento() {
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        Evento eventoSalvo = eventoService.cadastrarEvento(eventoDTO);

        assertNotNull(eventoSalvo);
        assertEquals("Evento Teste", eventoSalvo.getNome());
        verify(eventoRepository, times(1)).save(any(Evento.class));
    }

    @Test
    void deveListarEventoPorId() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        Optional<Evento> eventoEncontrado = eventoService.listarEventoId(1L);

        assertTrue(eventoEncontrado.isPresent());
        assertEquals(1L, eventoEncontrado.get().getId());
        verify(eventoRepository, times(1)).findById(1L);
    }

    @Test
    void deveRetornarListaDeEventos() {
        when(eventoRepository.findAllByAtivoTrue()).thenReturn(List.of(evento));

        List<Evento> eventos = eventoService.listarEventos();

        assertFalse(eventos.isEmpty());
        assertEquals(1, eventos.size());
        verify(eventoRepository, times(1)).findAllByAtivoTrue();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistemEventos() {
        when(eventoRepository.findAllByAtivoTrue()).thenReturn(List.of());

        List<Evento> eventos = eventoService.listarEventos();

        assertTrue(eventos.isEmpty());
        verify(eventoRepository, times(1)).findAllByAtivoTrue();
    }

    @Test
    void deveAtualizarEvento() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(eventoRepository.save(any(Evento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EventoDTO eventoAtualizadoDTO = new EventoDTO(1L, "Novo Nome Evento", "Descricao Teste", "2025-02-12", "Local Teste", "Tipo Teste", servidor);
        Evento eventoAtualizado = eventoService.atualizarEvento(eventoAtualizadoDTO);

        assertNotNull(eventoAtualizado);
        assertEquals("Novo Nome Evento", eventoAtualizado.getNome());
        verify(eventoRepository, times(1)).findById(1L);
        verify(eventoRepository, times(1)).save(any(Evento.class));
    }


    @Test
    void deveLancarExcecaoQuandoEventoNaoExisteParaAtualizacao() {
        when(eventoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> eventoService.atualizarEvento(eventoDTO));

        verify(eventoRepository, times(1)).findById(anyLong());
        verify(eventoRepository, never()).save(any(Evento.class));
    }

    @Test
    void deveInativarEvento() {
        when(eventoRepository.getReferenceById(1L)).thenReturn(evento);

        eventoService.inativarEvento(1L);

        assertFalse(evento.isAtivo());
        verify(eventoRepository, times(1)).getReferenceById(1L);
        verify(eventoRepository, times(1)).save(evento);
    }

    @Test
    void deveDeletarEvento() {
        doNothing().when(eventoRepository).deleteById(1L);

        eventoService.deletarEvento(1L);

        verify(eventoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveRetornarVazioQuandoEventoNaoExistePorId() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Evento> eventoEncontrado = eventoService.listarEventoId(1L);

        assertTrue(eventoEncontrado.isEmpty());
        verify(eventoRepository, times(1)).findById(1L);
    }
}
