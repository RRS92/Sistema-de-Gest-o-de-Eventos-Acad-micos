package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Banco;
import jakarta.validation.constraints.NotBlank;

public record BancoDTO(
		Long id,
		@NotBlank
		String nomeBanco,
		@NotBlank
		String numConta,
		@NotBlank
		String agencia,
		@NotBlank
		String operacao) {

	//Construtor para cadastrar banco
	public BancoDTO(String nomeBanco, String numConta, String agencia, String operacao) {
		this(null, nomeBanco, numConta, agencia, operacao);
	}

	//Construtor para listar banco
	public BancoDTO(Banco banco) {
		this(banco.getId(), banco.getNomeBanco(), banco.getNumConta(), banco.getAgencia(), banco.getOperacao());
	}
	
	//Construtor para atualizar banco
	public BancoDTO(Long id, String nomeBanco, String numConta, String agencia, String operacao) {
		this.id = id;
		this.nomeBanco = nomeBanco;
		this.numConta = numConta;
		this.agencia = agencia;
		this.operacao = operacao;
	}	
}
