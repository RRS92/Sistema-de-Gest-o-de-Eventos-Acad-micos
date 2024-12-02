package br.edu.ifpe.gestaoacademica.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpe.gestaoacademica.controllers.dto.CursoDTO;
import br.edu.ifpe.gestaoacademica.entities.Curso;
import br.edu.ifpe.gestaoacademica.service.CursoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cursos")
@CrossOrigin(origins = "*")
public class CursoController {

	@Autowired
	private CursoService cursoService;

	@PostMapping
	@Transactional
	public ResponseEntity<Curso> cadastrarCurso(@RequestBody @Valid CursoDTO dadosCursoDTO) {
		Curso curso = cursoService.cadastrarCurso(dadosCursoDTO);
		return ResponseEntity.ok(curso);
	}

	@PutMapping
	@Transactional
	public ResponseEntity<CursoDTO> atualizarCurso(@RequestBody @Valid CursoDTO dadosCursoDTO) {
		Curso curso = cursoService.atualizarCurso(dadosCursoDTO);
		return ResponseEntity.ok(new CursoDTO(curso));
	}

	@GetMapping
	public List<CursoDTO> listarCurso() {
		return cursoService.listarCurso().stream().map(CursoDTO::new).toList();
	}

	@DeleteMapping("/deletar/{id}")
	@Transactional
	public ResponseEntity<Void> deletarCurso(@PathVariable Long id) {
		cursoService.deletarCurso(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> inativarCurso(@PathVariable Long id) {
		cursoService.inativarCurso(id);
		return ResponseEntity.noContent().build();
	}
}
