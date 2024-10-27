package br.edu.ifpe.gestaoacademica.controllers.dto;

import jakarta.validation.constraints.NotBlank;

public record CadastrarAlunoDTO(
		@NotBlank
		String matricula) {
}
