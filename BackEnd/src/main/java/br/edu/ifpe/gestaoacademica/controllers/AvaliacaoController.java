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

import br.edu.ifpe.gestaoacademica.controllers.dto.AvaliacaoDTO;
import br.edu.ifpe.gestaoacademica.entities.Avaliacao;
import br.edu.ifpe.gestaoacademica.service.AvaliacaoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/avaliacoes")
@CrossOrigin(origins = "*")
public class AvaliacaoController {
	
	@Autowired
	private AvaliacaoService avaliacaoService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<Avaliacao> cadastrarAvaliacao(@RequestBody @Valid AvaliacaoDTO dadosAvaliacaoDTO) {
		Avaliacao avaliacao = avaliacaoService.cadastrarAvaliacao(dadosAvaliacaoDTO);
		return ResponseEntity.ok(avaliacao);
	}
	
	@GetMapping
	public List<AvaliacaoDTO> listarAvaliacao() {
		return avaliacaoService.listarAvaliacao().stream().map(AvaliacaoDTO::new).toList();
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<Avaliacao> atualizarAvaliacao(@RequestBody @Valid AvaliacaoDTO dadosAvaliacaoDTO) {
		var avaliacao = avaliacaoService.atualizarAvaliacao(dadosAvaliacaoDTO);
		return ResponseEntity.ok(avaliacao);
	}
	
	@DeleteMapping("/deletar/{id}")
	@Transactional
	public ResponseEntity<Void> deletarAvaliacao(@PathVariable Long id) {
		avaliacaoService.deletarAvaliacao(id);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> inativarAvaliacao(@PathVariable Long id) {
		avaliacaoService.inativarAvaliacao(id);
		return ResponseEntity.noContent().build();
	}

}
