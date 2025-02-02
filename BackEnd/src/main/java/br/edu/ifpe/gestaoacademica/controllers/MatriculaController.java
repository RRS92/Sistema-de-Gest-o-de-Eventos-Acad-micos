package br.edu.ifpe.gestaoacademica.controllers;

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

import br.edu.ifpe.gestaoacademica.controllers.dto.MatriculaDTO;
import br.edu.ifpe.gestaoacademica.entities.Matricula;
import br.edu.ifpe.gestaoacademica.service.MatriculaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/matriculas")
@CrossOrigin(origins = "*")
public class MatriculaController {

	@Autowired
	private MatriculaService matriculaService;
	
	
	@PostMapping
	@Transactional
	public ResponseEntity<Matricula> cadastrarMatricula(@RequestBody @Valid MatriculaDTO dadosMatriculaDTO) {
		Matricula matricula = matriculaService.cadastrarMatricula(dadosMatriculaDTO);
		return ResponseEntity.ok(matricula);
	}

	@PutMapping
	@Transactional
	public ResponseEntity<MatriculaDTO> atualizarMatricula(@RequestBody @Valid MatriculaDTO dadosMatriculaDTO) {
		Matricula matricula = matriculaService.atualizarMatricula(dadosMatriculaDTO);
		return ResponseEntity.ok(new MatriculaDTO(matricula));
	}

	/*@GetMapping
	public List<MatriculaDTO> listarMatricula(){
		return matriculaService.listarMatriculas().stream().map(MatriculaDTO::new).toList();
	}*/
	
	
	@GetMapping("/aluno/{idAluno}")
    public ResponseEntity<Matricula> getMatriculaByAlunoId(@PathVariable Long idAluno) {
        Matricula matricula = matriculaService.getMatriculaByAlunoId(idAluno);
        return matricula != null ? ResponseEntity.ok(matricula) : ResponseEntity.notFound().build();
    }
		
	/*@GetMapping("/aluno/{idAluno}")
	    public ResponseEntity<Matricula> getMatriculaByAlunoId(@PathVariable Long idAluno) {
		 matriculaService.listarMatriculas(idAluno)
	        Matricula matricula = matriculaRepository.findByAlunoId(idAluno);
	        return matricula != null ? ResponseEntity.ok(matricula) : ResponseEntity.notFound().build();
	*/

	@DeleteMapping("/deletar/{id}")
	@Transactional
	public ResponseEntity<Void> deletarMatricula(@PathVariable Long id) {
		matriculaService.deletarMatricula(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> inativarMatricula(@PathVariable Long id) {
		matriculaService.inativarMatricula(id);
		return ResponseEntity.noContent().build();
	}
}
