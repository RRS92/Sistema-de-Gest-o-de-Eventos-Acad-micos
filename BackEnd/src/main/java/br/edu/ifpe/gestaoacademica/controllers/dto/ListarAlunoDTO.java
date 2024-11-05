package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Aluno;
import br.edu.ifpe.gestaoacademica.entities.Banco;
import br.edu.ifpe.gestaoacademica.entities.Endereco;

public record ListarAlunoDTO(
		Long id,
		String matricula,
		String nome,
		String cpf,
		String rg,
		String dataNasc,
		String telefone,
		String email,
		Endereco endereco,
		Banco banco) {
	
	public ListarAlunoDTO(Aluno aluno) {
		this(aluno.getId(), aluno.getMatricula(), aluno.getNome(), aluno.getCpf(), aluno.getRg(), 
				aluno.getDataNasc(), aluno.getTelefone(), aluno.getEmail(), aluno.getEndereco(), aluno.getBanco());
	}
}
