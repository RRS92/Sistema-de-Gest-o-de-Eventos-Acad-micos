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
	
	@GetMapping("/{id}")
	public ResponseEntity<ServidorDTO> listarServidorId(@PathVariable Long id) {
	    Optional<Servidor> servidorOptional = servidorService.listarServidor(id);
	    
	    if (servidorOptional.isEmpty()) {
	        return ResponseEntity.notFound().build();  // Retorna 404 caso o servidor não seja encontrado
	    }

	    ServidorDTO servidorDTO = new ServidorDTO(servidorOptional.get());  // Converte para o DTO
	    return ResponseEntity.ok(servidorDTO);  // Retorna o servidor encontrado com status 200
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<Servidor> atualizarServidor(@RequestBody @Valid ServidorDTO dadosServidorDTO) {
		Servidor servidor = servidorService.atualizarServidor(dadosServidorDTO);
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
