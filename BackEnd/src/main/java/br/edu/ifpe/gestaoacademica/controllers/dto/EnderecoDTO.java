package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Endereco;
import jakarta.validation.constraints.NotBlank;

public record EnderecoDTO(
		Long id,
		@NotBlank
		String rua,
		@NotBlank
		String numero,
		@NotBlank
		String bairro,
		@NotBlank
		String cidade,
		@NotBlank
		String estado,
		@NotBlank
		String cep,
		@NotBlank
		String complemento) {

	
	//Construtor para cadastrar endereco
	public EnderecoDTO(String rua, String numero, String bairro, String cidade, String estado, String cep, String complemento) {
		this(null, rua, numero, bairro, cidade, estado, cep, complemento);
	}	
	
	//Construtor para listar endereco
	public EnderecoDTO(Endereco endereco) {
		this(endereco.getId(), endereco.getRua(), endereco.getNumero(), endereco.getBairro(), 
			endereco.getCidade(), endereco.getEstado(), endereco.getCep(), endereco.getComplemento());	
	}
	
	//Construtor para atualizar endereco
	public EnderecoDTO(Long id, String rua, String numero, String bairro, String cidade, String estado, String cep, String complemento) {
		this.id = id;
		this.rua = rua;
		this.numero = numero;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
		this.cep = cep;
		this.complemento = complemento;
	}	
}
