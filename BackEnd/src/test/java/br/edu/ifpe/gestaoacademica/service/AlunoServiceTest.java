package br.edu.ifpe.gestaoacademica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.ifpe.gestaoacademica.controllers.dto.AlunoDTO;
import br.edu.ifpe.gestaoacademica.entities.Aluno;
import br.edu.ifpe.gestaoacademica.entities.Banco;
import br.edu.ifpe.gestaoacademica.entities.Endereco;
import br.edu.ifpe.gestaoacademica.entities.Participante;
import br.edu.ifpe.gestaoacademica.entities.Utilizador;
import br.edu.ifpe.gestaoacademica.repository.AlunoRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AlunoServiceTest {

    @InjectMocks
    private AlunoService alunoService;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private Endereco endereco;

    @Mock
    private Banco banco;

    @Mock
    private List<Participante> participantes;

    @Mock
    private Utilizador utilizador;

    private AlunoDTO alunoDTO;
    private Aluno aluno;

    @BeforeEach
    void setUp() {
        // Criando o DTO de aluno com base no construtor fornecido
        alunoDTO = new AlunoDTO(
            "123456", "Nome Teste", "123.456.789-00", "12.345.678", "01/01/2000", 
            "1234-5678", "teste@teste.com", endereco, banco, participantes, utilizador
        );

        // Criando o aluno para ser usado nas verificações
        aluno = new Aluno();
        aluno.setId(1l);
        aluno.setMatricula("123456");
        aluno.setNome("Nome Teste");
        aluno.setCpf("123.456.789-00");
        aluno.setRg("12.345.678");
        aluno.setDataNasc("01/01/2000");
        aluno.setEmail("teste@teste.com");
        aluno.setTelefone("1234-5678");
        aluno.setBanco(banco);
        aluno.setEndereco(endereco);
        aluno.setAtivo(true);
        aluno.setParticipante(participantes);
        aluno.setUtilizador(utilizador);
    }
    
    
    
    @Test
    void cadastrarAluno_DeveSalvarAluno() {
        // Simulando a resposta do repositório
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        // Chamando o método de cadastro
        Aluno resultado = alunoService.cadastrarAluno(alunoDTO);

        // Verificando os resultados
        assertNotNull(resultado);
        assertEquals("123.456.789-00", resultado.getCpf());  // Verifica se o CPF foi preenchido corretamente

        // Verificando se o save foi chamado
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    void listarAlunos_DeveRetornarListaDeAlunosAtivos() {
        // Simulando a resposta do repositório
        when(alunoRepository.findAllByAtivoTrue()).thenReturn(List.of(aluno));

        // Chamando o método de listar alunos
        List<Aluno> alunos = alunoService.listarAlunos();

        // Verificando os resultados
        assertNotNull(alunos);
        assertFalse(alunos.isEmpty());
        assertEquals(1, alunos.size());
        assertEquals("Nome Teste", alunos.get(0).getNome());
        
        // Verificando se a consulta foi feita
        verify(alunoRepository, times(1)).findAllByAtivoTrue();
    }

    @Test
    void listarAluno_DeveRetornarAlunoPorId() {
        // Simulando a resposta do repositório
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        // Chamando o método de listar aluno
        Aluno resultado = alunoService.listarAluno(1L);
        assertNotNull(resultado);
        assertEquals("Nome Teste", resultado.getNome());


        // Verificando se a consulta foi feita
        verify(alunoRepository, times(1)).findById(1L);
    }

    @Test
    void listarAluno_DeveLancarExceptionSeNaoEncontrarAluno() {
        // Simulando a resposta do repositório
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());

        // Chamando o método de listar aluno e verificando se a exceção é lançada
        assertThrows(EntityNotFoundException.class, () -> alunoService.listarAluno(1L));

        // Verificando se a consulta foi feita
        verify(alunoRepository, times(1)).findById(1L);
    }

    @Test
    void atualizarAluno_DeveAtualizarDadosDoAluno() {
        // Simulando a resposta do repositório
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        // Criando um novo DTO de aluno para atualizar
        AlunoDTO alunoDTOAtualizado = new AlunoDTO(1l,
            "654321", "Nome Atualizado", "987.654.321-00", "34.567.890", "02/02/2001", 
            "9876-5432", "atualizado@teste.com", null, null, null, null
        );

        // Chamando o método de atualização
        Aluno resultado = alunoService.atualizarAluno(alunoDTOAtualizado);

        // Verificando os resultados
        assertEquals("Nome Atualizado", resultado.getNome());

        // Verificando se o save foi chamado
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    void inativarAluno_DeveMarcarAlunoComoInativo() {
        // Simulando a resposta do repositório
        when(alunoRepository.getReferenceById(1L)).thenReturn(aluno);

        // Chamando o método de inativação
        alunoService.inativarAluno(1L);

        // Verificando se o aluno foi marcado como inativo
        assertFalse(aluno.isAtivo());

        // Verificando se o save foi chamado
        verify(alunoRepository, times(1)).save(aluno);
    }

    @Test
    void deletarAluno_DeveDeletarAluno() {
        // Simulando a deleção
        doNothing().when(alunoRepository).deleteById(1L);

        // Chamando o método de deleção
        alunoService.deletarAluno(1L);

        // Verificando se o delete foi chamado
        verify(alunoRepository, times(1)).deleteById(1L);
    }
}
