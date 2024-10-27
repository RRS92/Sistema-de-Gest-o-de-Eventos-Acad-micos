package br.edu.ifpe.gestaoacademica.controllers.dto;

import jakarta.validation.constraints.NotBlank;

public record CadastrarServidorDTO(
		@NotBlank
		String siape,
		@NotBlank
		String cargo) {
}
