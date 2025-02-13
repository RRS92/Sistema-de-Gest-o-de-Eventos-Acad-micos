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

import br.edu.ifpe.gestaoacademica.controllers.dto.CertificadoDTO;
import br.edu.ifpe.gestaoacademica.entities.Certificado;
import br.edu.ifpe.gestaoacademica.entities.Evento;
import br.edu.ifpe.gestaoacademica.repository.CertificadoRepository;
import br.edu.ifpe.gestaoacademica.repository.EventoRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class CertificadoServiceTest {

    @Mock
    private CertificadoRepository certificadoRepository;

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private CertificadoService certificadoService;

    private Certificado certificado;
    private CertificadoDTO certificadoDTO;
    private Evento evento;

    @BeforeEach
    void setUp() {
        evento = new Evento();
        evento.setId(1L);
        evento.setNome("Evento Teste");

        certificado = new Certificado();
        certificado.setId(1L);
        certificado.setCargaHoraria("10h");
        certificado.setDescricao("Certificado Teste");
        certificado.setEvento(evento);
        certificado.setAtivo(true);

        certificadoDTO = new CertificadoDTO(1L, "10h", "Certificado Teste", 1L);
    }

    @Test
    void deveCadastrarCertificado() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(certificadoRepository.save(any(Certificado.class))).thenReturn(certificado);

        Certificado certificadoSalvo = certificadoService.cadastrarCertificado(certificadoDTO);
        assertNotNull(certificadoSalvo);
        assertEquals("10h", certificadoSalvo.getCargaHoraria());
    }

    @Test
    void deveLancarExcecaoAoCadastrarCertificadoComEventoInexistente() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> certificadoService.cadastrarCertificado(certificadoDTO));
    }

    @Test
    void deveListarCertificados() {
        when(certificadoRepository.findAllByAtivoTrue()).thenReturn(List.of(certificado));
        List<Certificado> certificados = certificadoService.listarCertificados();
        assertFalse(certificados.isEmpty());
        assertEquals(1, certificados.size());
    }

    @Test
    void deveListarCertificadosPorEvento() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(certificadoRepository.findByEvento(evento)).thenReturn(List.of(certificado));

        List<Certificado> certificados = certificadoService.listarCertificadosPorEvento(1L);
        assertFalse(certificados.isEmpty());
        assertEquals(1, certificados.size());
    }

    @Test
    void deveAtualizarCertificadoMantendoEvento() {
        when(certificadoRepository.findById(1L)).thenReturn(Optional.of(certificado));
        when(certificadoRepository.save(any(Certificado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CertificadoDTO certificadoAtualizadoDTO = new CertificadoDTO(1L, "15h", "Descricao Atualizada", 1L);
        Certificado certificadoAtualizado = certificadoService.atualizarCertificado(certificadoAtualizadoDTO);

        assertNotNull(certificadoAtualizado);
        assertEquals("15h", certificadoAtualizado.getCargaHoraria());
        assertEquals("Descricao Atualizada", certificadoAtualizado.getDescricao());
        assertEquals(1L, certificadoAtualizado.getEvento().getId()); // Garantindo que o evento não foi alterado
    }

    @Test
    void deveLancarExcecaoQuandoCertificadoNaoExisteParaAtualizacao() {
        when(certificadoRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> certificadoService.atualizarCertificado(certificadoDTO));
    }

    @Test
    void deveInativarCertificado() {
        when(certificadoRepository.findById(1L)).thenReturn(Optional.of(certificado));
        
        certificadoService.inativarCertificado(1L);
        
        assertFalse(certificado.isAtivo());  
        verify(certificadoRepository).save(certificado);  
    }


    @Test
    void deveLancarExcecaoAoInativarCertificadoInexistente() {
        when(certificadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> certificadoService.inativarCertificado(99L));
    }


    @Test
    void deveDeletarCertificado() {
        doNothing().when(certificadoRepository).deleteById(1L);
        certificadoService.deletarCertificado(1L);
        verify(certificadoRepository, times(1)).deleteById(1L);
    }
}