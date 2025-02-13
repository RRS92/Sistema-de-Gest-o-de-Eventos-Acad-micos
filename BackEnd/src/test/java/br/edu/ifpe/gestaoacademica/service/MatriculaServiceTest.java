package br.edu.ifpe.gestaoacademica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.ifpe.gestaoacademica.controllers.dto.MatriculaDTO;
import br.edu.ifpe.gestaoacademica.entities.Aluno;
import br.edu.ifpe.gestaoacademica.entities.Matricula;
import br.edu.ifpe.gestaoacademica.repository.MatriculaRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class MatriculaServiceTest {
	 @Mock
	    private MatriculaRepository matriculaRepository;

	    @InjectMocks
	    private MatriculaService matriculaService;

	    private MatriculaDTO matriculaDTO;
	    private Matricula matricula;

	    @BeforeEach
	    void setUp() {
	    	Aluno aluno = new Aluno(); 

	        matriculaDTO = new MatriculaDTO(1L, "2023.1", "Noturno", aluno, "Engenharia", "Presencial");
	        matricula = new Matricula();
	        matricula.setId(1L);
	        matricula.setPeriodoIngresso("2023.1");
	        matricula.setTurno("Noturno");
	        matricula.setNomeCurso("Engenharia");
	        matricula.setModalidade("Presencial");
	        matricula.setAtivo(true);
	    }

	    @Test
	    void testCadastrarMatricula() {
	        when(matriculaRepository.save(any(Matricula.class))).thenReturn(matricula);
	        
	        Matricula result = matriculaService.cadastrarMatricula(matriculaDTO);
	        
	        assertNotNull(result);
	        assertEquals("2023.1", result.getPeriodoIngresso());
	        assertEquals("Noturno", result.getTurno());
	        verify(matriculaRepository, times(1)).save(any(Matricula.class));
	    }
	    
	    @Test
	    void testAtualizarMatricula() {
	        when(matriculaRepository.findById(anyLong())).thenReturn(Optional.of(matricula));
	        when(matriculaRepository.save(any(Matricula.class))).thenReturn(matricula);

	        Matricula result = matriculaService.atualizarMatricula(matriculaDTO);
	        
	        assertNotNull(result);
	        assertEquals("2023.1", result.getPeriodoIngresso());
	        assertEquals("Noturno", result.getTurno());
	        verify(matriculaRepository, times(1)).findById(anyLong());
	        verify(matriculaRepository, times(1)).save(any(Matricula.class));
	    }
	    
	    @Test
	    void testAtualizarMatriculaNotFound() {
	        when(matriculaRepository.findById(anyLong())).thenReturn(Optional.empty());

	        assertThrows(EntityNotFoundException.class, () -> matriculaService.atualizarMatricula(matriculaDTO));
	        verify(matriculaRepository, times(1)).findById(anyLong());
	    }

	    @Test
	    void testGetMatriculaByAlunoId() {
	        when(matriculaRepository.findByAlunoId(anyLong())).thenReturn(matricula);
	        
	        Matricula result = matriculaService.getMatriculaByAlunoId(1L);
	        
	        assertNotNull(result);
	        assertEquals(1L, result.getId());
	        verify(matriculaRepository, times(1)).findByAlunoId(1L);
	    }

	    @Test
	    void testInativarMatricula() {
	        when(matriculaRepository.getReferenceById(anyLong())).thenReturn(matricula);
	        
	        matriculaService.inativarMatricula(1L);
	        
	        assertFalse(matricula.isAtivo());
	        verify(matriculaRepository, times(1)).save(matricula);
	    }

	    @Test
	    void testDeletarMatricula() {
	        doNothing().when(matriculaRepository).deleteById(anyLong());
	        
	        matriculaService.deletarMatricula(1L);
	        
	        verify(matriculaRepository, times(1)).deleteById(1L);
	    }
}
