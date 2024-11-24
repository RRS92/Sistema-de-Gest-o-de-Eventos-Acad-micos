package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Avaliacao;
import jakarta.validation.constraints.NotBlank;

public record AvaliacaoDTO(
		Long id,
		@NotBlank
		String nota,
		@NotBlank
		String comentario) {

	//Construtor para cadastrar avaliacoes
	public AvaliacaoDTO(String nota, String comentario) {
		this(null, nota, comentario);
	}

	//Construtor para listar avaliacoes
	public AvaliacaoDTO(Avaliacao avaliacao) {
		this(avaliacao.getId(), avaliacao.getNota(), avaliacao.getComentario());
	}

	//Construtor para atualizar avaliacoes
	public AvaliacaoDTO(Long id, String nota, String comentario) {
		this.id = id; 
		this.nota = nota;
		this.comentario = comentario;
	}

}
