package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Banco;

public record ListaBancoDTO(
		Long id,
		String nome,
		String numConta,
		String agencia,
		String operacao
		
		) {
	
	public ListaBancoDTO(Banco banco) {
		this(
				banco.getId(),
				banco.getNome(),
				banco.getNumConta(),
				banco.getAgencia(),
				banco.getOperacao());
	}

}
