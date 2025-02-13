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

import br.edu.ifpe.gestaoacademica.controllers.dto.BancoDTO;
import br.edu.ifpe.gestaoacademica.entities.Banco;
import br.edu.ifpe.gestaoacademica.repository.BancoRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class BancoServiceTest {
	 @Mock
	    private BancoRepository bancoRepository;

	    @InjectMocks
	    private BancoService bancoService;

	    private BancoDTO bancoDTO;
	    private Banco banco;

	    @BeforeEach
	    void setUp() {
	        bancoDTO = new BancoDTO(1L, "Banco Teste", "123456", "001", "001");
	        banco = new Banco(1L, "Banco Teste", "123456", "001", "001", true,null);
	    }

	    @Test
	    void testCadastrarBanco() {
	        when(bancoRepository.save(Mockito.any(Banco.class))).thenReturn(banco);

	        Banco resultado = bancoService.cadastrarBanco(bancoDTO);

	        assertNotNull(resultado);
	        assertEquals("Banco Teste", resultado.getNomeBanco());
	        verify(bancoRepository, times(1)).save(Mockito.any(Banco.class));
	    }
	    @Test
	    void testAtualizarBanco() {
	        when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));
	        when(bancoRepository.save(Mockito.any(Banco.class))).thenReturn(banco);

	        Banco resultado = bancoService.atualizarBanco(bancoDTO);

	        assertNotNull(resultado);
	        assertEquals("Banco Teste", resultado.getNomeBanco());
	        verify(bancoRepository, times(1)).findById(1L);
	        verify(bancoRepository, times(1)).save(Mockito.any(Banco.class));
	    }
	    
	    @Test
	    void testAtualizarBancoBancoNaoEncontrado() {
	        when(bancoRepository.findById(1L)).thenReturn(Optional.empty());

	        assertThrows(EntityNotFoundException.class, () -> bancoService.atualizarBanco(bancoDTO));

	        verify(bancoRepository, times(1)).findById(1L);
	    }
	    @Test
	    void testListarBanco() {
	        when(bancoRepository.findAllByAtivoTrue()).thenReturn(List.of(banco));

	        var bancos = bancoService.listarBanco();

	        assertNotNull(bancos);
	        assertFalse(bancos.isEmpty());
	        verify(bancoRepository, times(1)).findAllByAtivoTrue();
	    }

	    @Test
	    void testListarBancoId() {
	        when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));

	        var resultado = bancoService.listarBancoId(1L);

	        assertTrue(resultado.isPresent());
	        assertEquals("Banco Teste", resultado.get().getNomeBanco());
	        verify(bancoRepository, times(1)).findById(1L);
	    }

	    @Test
	    void testInativarBanco() {
	        when(bancoRepository.getReferenceById(1L)).thenReturn(banco);
	        when(bancoRepository.save(Mockito.any(Banco.class))).thenReturn(banco);

	        bancoService.inativarBanco(1L);

	        verify(bancoRepository, times(1)).getReferenceById(1L);
	        verify(bancoRepository, times(1)).save(Mockito.any(Banco.class));
	    }

	    @Test
	    void testDeletarBanco() {
	        doNothing().when(bancoRepository).deleteById(1L);

	        bancoService.deletarBanco(1L);

	        verify(bancoRepository, times(1)).deleteById(1L);
	    }
}
