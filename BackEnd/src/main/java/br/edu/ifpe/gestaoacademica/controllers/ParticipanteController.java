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

import br.edu.ifpe.gestaoacademica.controllers.dto.ParticipanteDTO;
import br.edu.ifpe.gestaoacademica.entities.Participante;
import br.edu.ifpe.gestaoacademica.service.ParticipanteService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/participantes")
@CrossOrigin(origins = "*")
public class ParticipanteController {
	
	@Autowired
	private ParticipanteService participanteService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<Participante> cadastrarParticipante(@RequestBody @Valid ParticipanteDTO dadosParticipanteDTO) {
		Participante participante = participanteService.cadastrarParticipante(dadosParticipanteDTO);
		return ResponseEntity.ok(participante);
	}
	
	@GetMapping
	public List<ParticipanteDTO> listarParticipante() {
		return participanteService.listarParticipante().stream().map(ParticipanteDTO::new).toList();
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<Participante> atualizarParticipante(@RequestBody @Valid ParticipanteDTO dadosParticipanteDTO) {
		var participante = participanteService.atualizarParticipante(dadosParticipanteDTO);
		return ResponseEntity.ok(participante);
	}
	
	@DeleteMapping("/deletar/{id}")
	@Transactional
	public ResponseEntity<Void> deletarParticipante(@PathVariable Long id) {
		participanteService.deletarParticipante(id);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> inativarParticipante(@PathVariable Long id) {
		participanteService.inativarParticipante(id);
		return ResponseEntity.noContent().build();
	}

}
