package br.edu.ifpe.gestaoacademica.controllers.dto;

import jakarta.validation.constraints.NotBlank;

public record CadastrarEventoDTO(
		@NotBlank
		String nome,
		@NotBlank
		String descricao,
		@NotBlank
		String data,
		@NotBlank
		String local,
		@NotBlank
		String tipo) {
}
