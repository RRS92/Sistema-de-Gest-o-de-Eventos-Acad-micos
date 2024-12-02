package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Curso;
import jakarta.validation.constraints.NotBlank;

public record CursoDTO(
		Long id,
		@NotBlank
		String nome,
		@NotBlank
		String modalidade) {

	//Construtor para cadastrar curso
	public CursoDTO(String nome, String modalidade) {
		this(null, nome, modalidade);
	}

	//Construtor para listar curso
	public CursoDTO(Curso curso) {
		this(curso.getId(), curso.getNome(), curso.getModalidade());
	}
	
	//Construtor para atualizar curso
	public CursoDTO(Long id, String nome, String modalidade) {
		this.id = id;
		this.nome = nome;
		this.modalidade = modalidade;
	}	
}
