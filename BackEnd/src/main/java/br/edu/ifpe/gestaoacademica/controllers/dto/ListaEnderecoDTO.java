package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Endereco;

public record ListaEnderecoDTO(
		Long id,
		String rua,
		String numero,
		String bairro,
		String cidade,
		String estado,
		String cep,
		String complemento
		
		) {
	
	public ListaEnderecoDTO(Endereco endereco) {
		this(
				endereco.getId(),
				endereco.getRua(),
				endereco.getNumero(),
				endereco.getBairro(),
				endereco.getCidade(),
				endereco.getEstado(),
				endereco.getCep(),
				endereco.getComplemento());
		
	}

}
