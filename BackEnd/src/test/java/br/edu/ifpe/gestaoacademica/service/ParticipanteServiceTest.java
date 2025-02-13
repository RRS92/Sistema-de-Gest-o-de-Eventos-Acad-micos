package br.edu.ifpe.gestaoacademica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.ifpe.gestaoacademica.controllers.dto.ParticipanteDTO;
import br.edu.ifpe.gestaoacademica.entities.Aluno;
import br.edu.ifpe.gestaoacademica.entities.Certificado;
import br.edu.ifpe.gestaoacademica.entities.Evento;
import br.edu.ifpe.gestaoacademica.entities.Participante;
import br.edu.ifpe.gestaoacademica.repository.ParticipanteRepository;

@ExtendWith(MockitoExtension.class)
class ParticipanteServiceTest {

    @Mock
    private ParticipanteRepository participanteRepository;

    @InjectMocks
    private ParticipanteService participanteService;

    private Participante participante;
    private Evento evento;
    private ParticipanteDTO participanteDTO;

    @BeforeEach
    void setUp() {
        evento = new Evento();
        evento.setId(1L);

        Aluno aluno = new Aluno();
        Certificado certificado = new Certificado();

        participante = new Participante();
        participante.setId(1L);
        participante.setAtivo(true);
        participante.setAluno(aluno);
        participante.setEvento(evento);
        participante.setCertificado(certificado);

        participanteDTO = new ParticipanteDTO(aluno, evento, certificado);
    }

    @Test
    void testCadastrarParticipante_ComSucesso() {
        when(participanteRepository.save(any(Participante.class))).thenReturn(participante);

        Participante resultado = participanteService.cadastrarParticipante(participanteDTO);

        assertNotNull(resultado);
        assertTrue(resultado.isAtivo());
        assertEquals(evento, resultado.getEvento());

        verify(participanteRepository).save(any(Participante.class));
    }

    @Test
    void testListarParticipantesPorEvento() {
        when(participanteRepository.findParticipantesByEvento(1L)).thenReturn(Arrays.asList(participante));

        List<ParticipanteDTO> lista = participanteService.listarParticipantesPorEvento(1L);

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals(evento, lista.get(0).evento());
    }

    @Test
    void testListarParticipante() {
        when(participanteRepository.findAllByAtivoTrue()).thenReturn(Arrays.asList(participante));

        List<Participante> lista = participanteService.listarParticipante();

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertTrue(lista.get(0).isAtivo());
    }

    @Test
    void testInativarParticipante_ComSucesso() {
        when(participanteRepository.getReferenceById(1L)).thenReturn(participante);
        when(participanteRepository.save(any(Participante.class))).thenReturn(participante);

        participanteService.inativarParticipante(1L);

        assertFalse(participante.isAtivo()); // Confirma que foi inativado

        verify(participanteRepository).save(participante);
    }

    @Test
    void testDeletarParticipante() {
        lenient().when(participanteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(participanteRepository).deleteById(1L);

        participanteService.deletarParticipante(1L);

        verify(participanteRepository, times(1)).deleteById(1L);
    }



    @Test
    void testDeletarParticipante_ParticipanteNaoExiste() {
        when(participanteRepository.existsById(1L)).thenReturn(false);

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
            jakarta.persistence.EntityNotFoundException.class, () -> participanteService.deletarParticipante(1L));

        assertEquals("Participante não encontrado", exception.getMessage());
    }

}
