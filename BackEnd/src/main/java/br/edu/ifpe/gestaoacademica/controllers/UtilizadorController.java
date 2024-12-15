package br.edu.ifpe.gestaoacademica.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpe.gestaoacademica.controllers.dto.UtilizadorDTO;
import br.edu.ifpe.gestaoacademica.entities.Utilizador;
import br.edu.ifpe.gestaoacademica.infra.DadosTokenJWTdto;
import br.edu.ifpe.gestaoacademica.infra.TokenService;
import br.edu.ifpe.gestaoacademica.service.UtilizadorService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/login")
public class UtilizadorController {
	
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UtilizadorService utilizadorService;
	@PostMapping
	public ResponseEntity<?> efetuarLogin(@RequestBody @Valid UtilizadorDTO dados) {
		var token = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
		var autenticacao = authenticationManager.authenticate(token);

		var tokenJWT = tokenService.gerarToken((Utilizador) autenticacao.getPrincipal());
	    var idzador = (Utilizador) autenticacao.getPrincipal();

		return ResponseEntity.ok(new DadosTokenJWTdto(tokenJWT,idzador.getId()));
		
	}
    @CrossOrigin(origins = "http://127.0.0.1:5500")
	@PostMapping("/cadastrar")
	@Transactional
	public ResponseEntity<Utilizador> cadastrarUtilizador(@RequestBody @Valid UtilizadorDTO dadosUtilizadorDTO) {
		Utilizador utilizador= utilizadorService.cadastrarUtilizador(dadosUtilizadorDTO);
		return ResponseEntity.ok(utilizador);
	}

	
	
	@GetMapping
	public List<UtilizadorDTO> listarUtilizador() {
		return utilizadorService.listarUtilizador().stream().map(UtilizadorDTO::new).toList();
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<Utilizador> atualizarUtilizador(@RequestBody @Valid UtilizadorDTO dadosUtilizadorDTO) {
		Utilizador utilizador = utilizadorService.atualizarUtilizador(dadosUtilizadorDTO);
		return ResponseEntity.ok(utilizador);
	}

	@DeleteMapping("/deletar/{id}")
	@Transactional
	public ResponseEntity<Void> deletarUtilizador(@PathVariable Long id) {
		utilizadorService.deletarUtilizador(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> inativarUtilizador(@PathVariable Long id) {
	utilizadorService.inativarUtilizador(id);
		return ResponseEntity.noContent().build();
	}
}
