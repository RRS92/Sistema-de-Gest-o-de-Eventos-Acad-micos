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

import br.edu.ifpe.gestaoacademica.controllers.dto.AtualizarEnderecoDTO;
import br.edu.ifpe.gestaoacademica.controllers.dto.CadastrarEnderecoDTO;
import br.edu.ifpe.gestaoacademica.controllers.dto.ListaEnderecoDTO;
import br.edu.ifpe.gestaoacademica.entities.Endereco;
import br.edu.ifpe.gestaoacademica.service.EnderecoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/enderecos")
@CrossOrigin(origins = "*")
public class EnderecoController {
	
	@Autowired
	private EnderecoService enderecoService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<Endereco> cadastrarEndereco(@RequestBody @Valid CadastrarEnderecoDTO dadosEnderecoDTO){
		
		Endereco endereco = enderecoService.cadastrarEndereco(dadosEnderecoDTO);
		
		return ResponseEntity.ok(endereco);
		
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<AtualizarEnderecoDTO> atualizarEndereco(@RequestBody @Valid AtualizarEnderecoDTO dadosAtualizacao) {
	    Endereco enderecoAtualizado = enderecoService.atualizarEndereco(dadosAtualizacao);
	    return ResponseEntity.ok(new AtualizarEnderecoDTO(enderecoAtualizado));
	}
	
	@GetMapping
    public List<ListaEnderecoDTO> listar(){
		return enderecoService.listarEnderecos().stream().map(ListaEnderecoDTO::new).toList();
	}
	
	@DeleteMapping("/apagar/{id}")
	@Transactional
	public ResponseEntity<Void> excluir(@PathVariable Long id){
		enderecoService.deletarEndereco(id);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> inativar(@PathVariable Long id){
		enderecoService.inativarEndereco(id);
		return ResponseEntity.noContent().build();
	}

}
