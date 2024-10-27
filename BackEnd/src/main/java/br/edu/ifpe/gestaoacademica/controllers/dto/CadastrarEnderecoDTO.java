package br.edu.ifpe.gestaoacademica.controllers.dto;

import jakarta.validation.constraints.NotBlank;

public record CadastrarEnderecoDTO(
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
		String complemento
		) {

}
