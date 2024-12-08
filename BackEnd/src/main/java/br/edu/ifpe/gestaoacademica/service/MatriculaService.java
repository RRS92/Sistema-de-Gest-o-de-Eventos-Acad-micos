package br.edu.ifpe.gestaoacademica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.edu.ifpe.gestaoacademica.controllers.dto.MatriculaDTO;
import br.edu.ifpe.gestaoacademica.entities.Matricula;
import br.edu.ifpe.gestaoacademica.repository.MatriculaRepository;
import jakarta.persistence.EntityNotFoundException;

@CrossOrigin(origins = "*")

@Service
public class MatriculaService {

	@Autowired
	private MatriculaRepository matriculaRepository;

	public Matricula cadastrarMatricula(MatriculaDTO dadosMatriculaDTO) {
		
		Matricula matricula = new Matricula();
		matricula.setNumMatricula(dadosMatriculaDTO.numMatricula());
		matricula.setPeriodoIngresso(dadosMatriculaDTO.periodoIngresso());
		matricula.setTurno(dadosMatriculaDTO.turno());
		matricula.setAluno(dadosMatriculaDTO.aluno());
		matricula.setCurso(dadosMatriculaDTO.curso());

		matricula.setAtivo(true);

		return matriculaRepository.save(matricula);
	}

	public Matricula atualizarMatricula(MatriculaDTO dadosMatriculaDTO) {
		Matricula matricula = matriculaRepository.findById(dadosMatriculaDTO.id())
				.orElseThrow(() -> new EntityNotFoundException("Matricula não encontrado"));
 
		if (dadosMatriculaDTO.numMatricula() != null) 	  matricula.setNumMatricula(dadosMatriculaDTO.numMatricula());
		if (dadosMatriculaDTO.periodoIngresso() != null)  matricula.setPeriodoIngresso(dadosMatriculaDTO.periodoIngresso());
		if (dadosMatriculaDTO.turno() != null) 			  matricula.setTurno(dadosMatriculaDTO.turno());

		return matriculaRepository.save(matricula);
	}

	public List<Matricula> listarMatricula() {
		return matriculaRepository.findAllByAtivoTrue();
	}

	public void inativarMatricula(Long id) {
		Matricula matricula = matriculaRepository.getReferenceById(id);
		matricula.inativar();
		matriculaRepository.save(matricula);
	}

	public void deletarMatricula(Long id) {
		matriculaRepository.deleteById(id);
	}
}
