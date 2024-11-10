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

import br.edu.ifpe.gestaoacademica.controllers.dto.ServidorDTO;
import br.edu.ifpe.gestaoacademica.entities.Servidor;
import br.edu.ifpe.gestaoacademica.service.ServidorService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/servidores")
@CrossOrigin(origins = "*")
public class ServidorController {

	@Autowired
	private ServidorService servidorService;

	@PostMapping
	@Transactional
	public ResponseEntity<Servidor> cadastrarServidor(@RequestBody @Valid ServidorDTO dadosServidorDTO) {

		Servidor servidor = servidorService.cadastrarServidor(dadosServidorDTO);
		return ResponseEntity.ok(servidor);
	}

	@GetMapping
	public List<ServidorDTO> listarServidor() {
		return servidorService.listarServidores().stream().map(ServidorDTO::new).toList();
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<Servidor> atualizarServidor(@RequestBody @Valid ServidorDTO dadosServidorDTO) {
		var servidor = servidorService.atualizarServidor(dadosServidorDTO);
		return ResponseEntity.ok(servidor);
	}

	@DeleteMapping("/deletar/{id}")
	@Transactional
	public ResponseEntity<Void> deletarServidor(@PathVariable Long id) {
		servidorService.deletarServidor(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> inativarServidor(@PathVariable Long id) {
		servidorService.inativarServidor(id);
		return ResponseEntity.noContent().build();
	}
}
