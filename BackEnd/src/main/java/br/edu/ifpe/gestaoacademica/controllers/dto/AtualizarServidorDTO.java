package br.edu.ifpe.gestaoacademica.controllers.dto;

import jakarta.validation.constraints.NotNull;

public record AtualizarServidorDTO(
		@NotNull
		Long id,
		String siape,
		String cargo) {
}
