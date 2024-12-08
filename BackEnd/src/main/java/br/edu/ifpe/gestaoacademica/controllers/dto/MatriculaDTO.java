package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Aluno;
import br.edu.ifpe.gestaoacademica.entities.Curso;
import br.edu.ifpe.gestaoacademica.entities.Matricula;
import jakarta.validation.constraints.NotBlank;

public record MatriculaDTO(
		Long id,
		@NotBlank
		String numMatricula,
		@NotBlank
		String periodoIngresso,
		@NotBlank
		String turno,
		Aluno aluno,
		Curso curso) {

	//Construtor para cadastrar matricula
	public MatriculaDTO(String numMatricula, String periodoIngresso, String turno, Aluno aluno, Curso curso) {
		this(null, numMatricula, periodoIngresso, turno, aluno, curso);
	}

	//Construtor para listar matricula
	public MatriculaDTO(Matricula matricula) {
		this(matricula.getId(), matricula.getNumMatricula(), matricula.getPeriodoIngresso(), matricula.getTurno(), matricula.getAluno(), matricula.getCurso());
	}

	//Construtor para atualizar matricula
	public MatriculaDTO(Long id, String numMatricula, String periodoIngresso, String turno, Aluno aluno, Curso curso) {
		this.id = id;
		this.numMatricula = numMatricula;
		this.periodoIngresso = periodoIngresso;
		this.turno = turno;
		this.aluno = aluno;
		this.curso = curso;
	}	
}
