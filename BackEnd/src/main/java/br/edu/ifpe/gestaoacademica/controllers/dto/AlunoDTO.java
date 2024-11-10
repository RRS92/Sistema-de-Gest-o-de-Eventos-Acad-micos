package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Aluno;
import br.edu.ifpe.gestaoacademica.entities.Banco;
import br.edu.ifpe.gestaoacademica.entities.Endereco;

public record AlunoDTO(
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

	//Construtor para cadastrar aluno
	public AlunoDTO(String matricula, String nome, String cpf, String rg, String dataNasc, 
			String telefone, String email, Endereco endereco, Banco banco) {
		this(null, matricula, nome, cpf, rg, dataNasc, telefone, email, endereco, banco);
	}
	
	//Construtor para listar aluno
	public AlunoDTO(Aluno aluno) {
		this(aluno.getId(), aluno.getMatricula(), aluno.getNome(), aluno.getCpf(), aluno.getRg(), 
				aluno.getDataNasc(), aluno.getTelefone(), aluno.getEmail(), aluno.getEndereco(), aluno.getBanco());
	}

	//Construtor para atualizar aluno
	public AlunoDTO(Long id, String matricula, String nome, String dataNasc, 
			String telefone, String email, Endereco endereco, Banco banco) {
		this(id, matricula, nome, null, null, dataNasc, telefone, email, endereco, banco);
	}
}
