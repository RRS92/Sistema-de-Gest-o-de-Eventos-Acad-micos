package br.edu.ifpe.gestaoacademica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.edu.ifpe.gestaoacademica.controllers.dto.AtualizarAlunoDTO;
import br.edu.ifpe.gestaoacademica.controllers.dto.CadastrarAlunoDTO;
import br.edu.ifpe.gestaoacademica.entities.Aluno;
import br.edu.ifpe.gestaoacademica.repository.AlunoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")

@Service
public class AlunoService {

	@Autowired
	private AlunoRepository alunoRepository;

	public Aluno cadastrarAluno(CadastrarAlunoDTO dadosAlunoDTO) {

		Aluno aluno = new Aluno();
		aluno.setMatricula(dadosAlunoDTO.matricula());
		aluno.setAtivo(true);

		return alunoRepository.save(aluno);

	}

	public List<Aluno> listarAlunos() {
		return alunoRepository.findAllByAtivoTrue();
	}
	
	public Aluno atualizarAluno(@Valid AtualizarAlunoDTO dadosAlunoDTO) {
		
	    Aluno aluno = alunoRepository.findById(dadosAlunoDTO.id())
	            .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

		if (dadosAlunoDTO.matricula() != null) {
			aluno.setMatricula(dadosAlunoDTO.matricula());
		}
		
		return alunoRepository.save(aluno);
	}

	public void inativarAluno(Long id) {
		Aluno aluno = alunoRepository.getReferenceById(id);
		aluno.inativar();
		alunoRepository.save(aluno);
	}

	public void deletarAluno(Long id) {
		alunoRepository.deleteById(id);
	}
}
