package br.edu.ifpe.gestaoacademica.controllers.dto;

import jakarta.validation.constraints.NotBlank;

public record CadastrarBancoDTO(	
		@NotBlank
		String nome,
		@NotBlank
		String numConta,
		@NotBlank
		String agencia,
		@NotBlank
		String operacao
		) {
	
}
