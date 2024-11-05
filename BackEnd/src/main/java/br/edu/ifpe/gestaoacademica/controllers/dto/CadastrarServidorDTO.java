package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Banco;
import br.edu.ifpe.gestaoacademica.entities.Endereco;
import jakarta.validation.constraints.NotBlank;

public record CadastrarServidorDTO(
		@NotBlank
		String siape,
		@NotBlank
		String cargo,
		@NotBlank
		String nome,
		@NotBlank
		String cpf,
		@NotBlank
		String rg,
		@NotBlank
		String dataNasc,
		@NotBlank
		String telefone,
		@NotBlank
		String email,
		Endereco endereco,
		Banco banco) {
}
