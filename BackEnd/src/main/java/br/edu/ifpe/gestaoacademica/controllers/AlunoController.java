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

import br.edu.ifpe.gestaoacademica.controllers.dto.AtualizarAlunoDTO;
import br.edu.ifpe.gestaoacademica.controllers.dto.CadastrarAlunoDTO;
import br.edu.ifpe.gestaoacademica.controllers.dto.ListarAlunoDTO;
import br.edu.ifpe.gestaoacademica.entities.Aluno;
import br.edu.ifpe.gestaoacademica.service.AlunoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/alunos")
@CrossOrigin(origins = "*")
public class AlunoController {
	
	@Autowired
	private AlunoService alunoService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<Aluno> cadastrarAluno(@RequestBody @Valid CadastrarAlunoDTO dadosAlunoDTO) {
		
		Aluno aluno = alunoService.cadastrarAluno(dadosAlunoDTO);
			return ResponseEntity.ok(aluno);
	}
	
	@GetMapping
    public List<ListarAlunoDTO> listarAluno() {
        return alunoService.listarAlunos().stream().map(ListarAlunoDTO::new).toList();
    }
	
	@PutMapping
	@Transactional
	public ResponseEntity<Aluno> atualizarAluno(@RequestBody @Valid AtualizarAlunoDTO dadosAlunoDTO) {
		var aluno = alunoService.atualizarAluno(dadosAlunoDTO);
		return ResponseEntity.ok(aluno);
	}
	
	@DeleteMapping("/deletar/{id}")
	@Transactional
	public ResponseEntity<Void> deletarAluno(@PathVariable Long id) {
		alunoService.deletarAluno(id);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> inativarAluno(@PathVariable Long id) {
		alunoService.inativarAluno(id);
		return ResponseEntity.noContent().build();
	}
}
