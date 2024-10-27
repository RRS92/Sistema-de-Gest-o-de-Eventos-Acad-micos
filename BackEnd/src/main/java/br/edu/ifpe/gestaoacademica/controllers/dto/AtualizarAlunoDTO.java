package br.edu.ifpe.gestaoacademica.controllers.dto;

import jakarta.validation.constraints.NotNull;

public record AtualizarAlunoDTO(
		@NotNull
		Long id,
		String matricula) {
}
