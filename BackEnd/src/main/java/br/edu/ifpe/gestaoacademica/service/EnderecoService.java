package br.edu.ifpe.gestaoacademica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.edu.ifpe.gestaoacademica.controllers.dto.AtualizarEnderecoDTO;
import br.edu.ifpe.gestaoacademica.controllers.dto.CadastrarEnderecoDTO;
import br.edu.ifpe.gestaoacademica.entities.Endereco;
import br.edu.ifpe.gestaoacademica.repository.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;

@CrossOrigin(origins = "*")

@Service
public class EnderecoService {
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Endereco cadastrarEndereco(CadastrarEnderecoDTO dadosEnderecoDTO) {
		
		Endereco endereco = new Endereco();
		endereco.setRua(dadosEnderecoDTO.rua());
		endereco.setNumero(dadosEnderecoDTO.numero());
		endereco.setBairro(dadosEnderecoDTO.bairro());
		endereco.setCidade(dadosEnderecoDTO.cidade());
		endereco.setEstado(dadosEnderecoDTO.estado());
		endereco.setCep(dadosEnderecoDTO.cep());
		endereco.setComplemento(dadosEnderecoDTO.complemento());
		endereco.setAtivo(true);
		
		return enderecoRepository.save(endereco);
		
	}
	
	public Endereco atualizarEndereco(AtualizarEnderecoDTO dadosAtualizacao) {
	    Endereco endereco = enderecoRepository.findById(dadosAtualizacao.id())
	                                  .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado"));
	    if (dadosAtualizacao.rua() != null) endereco.setRua(dadosAtualizacao.rua());
	    if (dadosAtualizacao.numero() != null) endereco.setNumero(dadosAtualizacao.numero());
	    if (dadosAtualizacao.bairro() != null) endereco.setBairro(dadosAtualizacao.bairro());
	    if (dadosAtualizacao.cidade() != null) endereco.setCidade(dadosAtualizacao.cidade());
	    if (dadosAtualizacao.estado() != null) endereco.setEstado(dadosAtualizacao.estado());
	    if (dadosAtualizacao.cep() != null) endereco.setCep(dadosAtualizacao.cep());
	    if (dadosAtualizacao.complemento() != null) endereco.setComplemento(dadosAtualizacao.complemento());

	    return enderecoRepository.save(endereco);
	}
	
	public List<Endereco> listarEnderecos(){
		return enderecoRepository.findAllByAtivoTrue();
	}
	
	public void inativarEndereco(Long id) {
		Endereco endereco = enderecoRepository.getReferenceById(id);
		endereco.inativar();
		enderecoRepository.save(endereco);
	}
	
	public void deletarEndereco(Long id) {
		
		enderecoRepository.deleteById(id);
		;	
	}
}
