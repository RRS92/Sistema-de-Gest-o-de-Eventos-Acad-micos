package br.edu.ifpe.gestaoacademica.controllers;

import java.util.List;
import java.util.Optional;

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

import br.edu.ifpe.gestaoacademica.controllers.dto.AlunoDTO;
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
	public ResponseEntity<Aluno> cadastrarAluno(@RequestBody @Valid AlunoDTO dadosAlunoDTO) {
		Aluno aluno = alunoService.cadastrarAluno(dadosAlunoDTO);
			return ResponseEntity.ok(aluno);
	}
	
	@GetMapping
    public List<AlunoDTO> listarAluno() {
        return alunoService.listarAlunos().stream().map(AlunoDTO::new).toList();
    }
	
	
	@GetMapping("/{id}")
	public ResponseEntity<AlunoDTO> listarAlunoId(@PathVariable Long id) {
	    Optional<Aluno> alunoOptional = Optional.ofNullable(alunoService.listarAluno(id));
	    
	    if (alunoOptional.isEmpty()) {
	        return ResponseEntity.notFound().build();  // Retorna 404 caso o aluno não seja encontrado
	    }

	    AlunoDTO alunoDTO = new AlunoDTO(alunoOptional.get());  // Converte para o DTO
	    return ResponseEntity.ok(alunoDTO);  // Retorna o aluno encontrado com status 200
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<Aluno> atualizarAluno(@RequestBody @Valid AlunoDTO dadosAlunoDTO) {
		Aluno aluno = alunoService.atualizarAluno(dadosAlunoDTO);
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
