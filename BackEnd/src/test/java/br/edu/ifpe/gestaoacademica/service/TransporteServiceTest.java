package br.edu.ifpe.gestaoacademica.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

import br.edu.ifpe.gestaoacademica.controllers.dto.TransporteDTO;
import br.edu.ifpe.gestaoacademica.entities.Evento;
import br.edu.ifpe.gestaoacademica.entities.Servidor;
import br.edu.ifpe.gestaoacademica.entities.Transporte;
import br.edu.ifpe.gestaoacademica.repository.EventoRepository;
import br.edu.ifpe.gestaoacademica.repository.TransporteRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class TransporteServiceTest {

    @Mock
    private TransporteRepository transporteRepository;

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private TransporteService transporteService;

    private Transporte transporte;
    private Evento evento;
    private TransporteDTO transporteDTO;

    @BeforeEach
    void setUp() {
        evento = new Evento();
        evento.setId(1L);

        transporte = new Transporte();
        transporte.setId(1L);
        transporte.setCategoria("Ônibus");
        transporte.setPlaca("ABC-1234");
        transporte.setQuilometragem("200km");
        transporte.setNomeMotorista("João");
        transporte.setHoraSaida("08:00");
        transporte.setHoraChegada("12:00");
        transporte.setAtivo(true);
        transporte.setEvento(evento);
        transporte.setServidores(Arrays.asList(new Servidor()));

        transporteDTO = new TransporteDTO("Ônibus", "ABC-1234", "200km", "João", "08:00", "12:00", 1L, Arrays.asList(new Servidor()));
    }

    @Test
    void testCadastrarTransporte_ComSucesso() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(transporteRepository.save(any(Transporte.class))).thenReturn(transporte);

        Transporte resultado = transporteService.cadastrarTransporte(transporteDTO);

        assertNotNull(resultado);
        assertTrue(resultado.isAtivo());
        assertEquals("Ônibus", resultado.getCategoria());

        verify(transporteRepository).save(any(Transporte.class));
    }
    
    @Test
    void testCadastrarTransporte_ComEventoNaoExistente_DeveLancarExcecao() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, 
            () -> transporteService.cadastrarTransporte(transporteDTO));

        assertEquals("Evento não encontrado", exception.getMessage());
    }


    @Test
    void testListarTransporte() {
        when(transporteRepository.findAllByAtivoTrue()).thenReturn(Arrays.asList(transporte));

        List<Transporte> lista = transporteService.listarTransporte();

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertTrue(lista.get(0).isAtivo());
    }

    @Test
    void testListarTransportesPorEvento() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(transporteRepository.findByEvento(evento)).thenReturn(Arrays.asList(transporte));

        List<Transporte> lista = transporteService.listarTransportesPorEvento(1L);

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("Ônibus", lista.get(0).getCategoria());
    }

    @Test
    void testInativarTransporte_ComSucesso() {
        when(transporteRepository.getReferenceById(1L)).thenReturn(transporte);

        transporteService.inativarTransporte(1L);

        assertFalse(transporte.isAtivo()); // Certifica que o transporte foi inativado
        verify(transporteRepository).save(transporte);
    }

    
    @Test
    void testAtualizarTransporte_ComSucesso() {
        when(transporteRepository.findById(1L)).thenReturn(Optional.of(transporte));
        when(transporteRepository.save(any(Transporte.class))).thenReturn(transporte);

        TransporteDTO novoDTO = new TransporteDTO(1L, "Van", "XYZ-5678", "300km", "Carlos", "10:00", "14:00", 1L, Arrays.asList(new Servidor()));

        Transporte atualizado = transporteService.atualizarTransporte(novoDTO);

        assertEquals("Van", atualizado.getCategoria());
        assertEquals("XYZ-5678", atualizado.getPlaca());
        assertEquals("Carlos", atualizado.getNomeMotorista());
        verify(transporteRepository).save(any(Transporte.class));
    }

    @Test
    void testAtualizarTransporte_ComTransporteNaoExistente_DeveLancarExcecao() {
        when(transporteRepository.findById(99L)).thenReturn(Optional.empty());

        TransporteDTO dtoInvalido = new TransporteDTO(99L, "Van", "XYZ-5678", "300km", "Carlos", "10:00", "14:00", 1L, Arrays.asList(new Servidor()));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, 
            () -> transporteService.atualizarTransporte(dtoInvalido));

        assertEquals("Transporte não encontrado", exception.getMessage());
    }


    @Test
    void testDeletarTransporte() {
        doNothing().when(transporteRepository).deleteById(1L);

        transporteService.deletarTransporte(1L);

        verify(transporteRepository, times(1)).deleteById(1L);
    }
    
    
    
    @Test
    void testDeletarTransporte_ComTransporteNaoExistente() {
        doNothing().when(transporteRepository).deleteById(99L);

        assertDoesNotThrow(() -> transporteService.deletarTransporte(99L));

        verify(transporteRepository, times(1)).deleteById(99L);
    }

}
