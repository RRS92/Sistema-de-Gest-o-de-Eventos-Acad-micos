package br.edu.ifpe.gestaoacademica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.edu.ifpe.gestaoacademica.controllers.dto.CursoDTO;
import br.edu.ifpe.gestaoacademica.entities.Curso;
import br.edu.ifpe.gestaoacademica.repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;

@CrossOrigin(origins = "*")

@Service
public class CursoService {

	@Autowired
	private CursoRepository cursoRepository;

	public Curso cadastrarCurso(CursoDTO dadosCursoDTO) {

		Curso curso = new Curso();
		curso.setNome(dadosCursoDTO.nome());
		curso.setModalidade(dadosCursoDTO.modalidade());
		curso.setAtivo(true);

		return cursoRepository.save(curso);
	}

	public Curso atualizarCurso(CursoDTO dadosCursoDTO) {
		Curso curso = cursoRepository.findById(dadosCursoDTO.id())
				.orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

		if (dadosCursoDTO.nome() != null) 		curso.setNome(dadosCursoDTO.nome());
		if (dadosCursoDTO.modalidade() != null) curso.setModalidade(dadosCursoDTO.modalidade());

		return cursoRepository.save(curso);
	}

	public List<Curso> listarCurso() {
		return cursoRepository.findAllByAtivoTrue();
	}

	public void inativarCurso(Long id) {
		Curso curso = cursoRepository.getReferenceById(id);
		curso.inativar();
		cursoRepository.save(curso);
	}

	public void deletarCurso(Long id) {
		cursoRepository.deleteById(id);
	}
}
