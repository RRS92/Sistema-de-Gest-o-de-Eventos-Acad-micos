package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Banco;

public record ListarBancoDTO(
		Long id,
		String nomeBanco,
		String numConta,
		String agencia,
		String operacao) {
	
	public ListarBancoDTO(Banco banco) {
		this(banco.getId(), banco.getNomeBanco(), banco.getNumConta(), banco.getAgencia(), banco.getOperacao());
	}
}
