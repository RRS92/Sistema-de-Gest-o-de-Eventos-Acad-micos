package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Aluno;

public record ListarAlunoDTO(
		Long id,
		String matricula) {
	
	public ListarAlunoDTO(Aluno aluno) {
		this(aluno.getId(), aluno.getMatricula());
	}
}
