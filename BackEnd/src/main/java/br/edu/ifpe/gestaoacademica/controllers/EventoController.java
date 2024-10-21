package br.edu.ifpe.gestaoacademica.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpe.gestaoacademica.controllers.dto.CadastrarEventoDTO;
import br.edu.ifpe.gestaoacademica.entities.Evento;
import br.edu.ifpe.gestaoacademica.service.EventoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/eventos")
@CrossOrigin(origins = "*")
public class EventoController {

	@Autowired
	private EventoService eventoService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<Evento> cadastrarEvento(@RequestBody @Valid CadastrarEventoDTO dadosEventoDTO) {

	Evento evento = eventoService.cadastrarEvento(dadosEventoDTO);

		return ResponseEntity.ok(evento);

	}
}
