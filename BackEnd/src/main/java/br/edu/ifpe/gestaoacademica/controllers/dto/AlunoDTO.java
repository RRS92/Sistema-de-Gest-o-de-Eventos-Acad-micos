package br.edu.ifpe.gestaoacademica.controllers.dto;

import java.util.List;

import br.edu.ifpe.gestaoacademica.entities.Aluno;
import br.edu.ifpe.gestaoacademica.entities.Banco;
import br.edu.ifpe.gestaoacademica.entities.Endereco;
import br.edu.ifpe.gestaoacademica.entities.Participante;
import br.edu.ifpe.gestaoacademica.entities.Utilizador;
import jakarta.validation.constraints.NotBlank;

public record AlunoDTO(
		Long id,
		//@NotBlank
		String matricula,
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
		Banco banco,
		List <Participante> participante,
		Utilizador utilizador

) { 

	//Construtor para cadastrar aluno
	public AlunoDTO(String matricula,String nome, String cpf, String rg, String dataNasc, 
			        String telefone, String email, Endereco endereco, Banco banco,
			        List <Participante> participante,Utilizador utilizador) {
		this(null,matricula,nome, cpf, rg, dataNasc, telefone, email, endereco, banco,participante,utilizador);
	}
	
	//Construtor para listar aluno
	public AlunoDTO(Aluno aluno) {
		this(aluno.getId(),aluno.getMatricula(), aluno.getNome(), aluno.getCpf(), aluno.getRg(), 
			 aluno.getDataNasc(), aluno.getTelefone(), aluno.getEmail(), aluno.getEndereco(), aluno.getBanco(),aluno.getParticipante(),aluno.getUtilizador()
		        );
	}

	//Construtor para atualizar aluno
	public AlunoDTO(Long id, String matricula, String nome, String cpf, String rg, String dataNasc, 
			        String telefone, String email, Endereco endereco, Banco banco,List <Participante> participante,Utilizador utilizador) {
		this.id = id;
		this.matricula=matricula;
		this.nome = nome;
		this.cpf = cpf;
		this.rg = rg;
		this.dataNasc = dataNasc;
		this.telefone = telefone;
		this.email = email;
		this.endereco = endereco;
		this.banco = banco;
		this.participante = participante;
		this.utilizador = utilizador;

	}
}
