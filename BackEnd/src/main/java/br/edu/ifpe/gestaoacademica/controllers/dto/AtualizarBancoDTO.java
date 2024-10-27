package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Banco;
import jakarta.validation.constraints.NotNull;

public record AtualizarBancoDTO(
		
		@NotNull
		Long id,
		String nome,
		String numConta,
		String agencia,
		String operacao) {
	
	public AtualizarBancoDTO(Banco banco) {
		this(banco.getId(), banco.getNome(), banco.getNumConta(), banco.getAgencia(), banco.getOperacao());
		
			
	}
	
}
