package br.edu.ifpe.gestaoacademica.controllers.dto;

import jakarta.validation.constraints.NotNull;

public record AtualizarEventoDTO(
		@NotNull
		Long id,
		String nome,
		String descricao,
		String data,
		String local,
		String tipo) {
}
