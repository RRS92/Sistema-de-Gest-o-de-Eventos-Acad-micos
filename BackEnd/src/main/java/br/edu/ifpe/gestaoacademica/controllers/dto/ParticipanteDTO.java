package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Participante;
import jakarta.validation.constraints.NotBlank;

public record ParticipanteDTO(
		Long id,
		@NotBlank
		String nomeSocial
		) {

	//Construtor para cadastrar participantes
	public ParticipanteDTO(String nomeSocial) {
		this(null, nomeSocial);
	}

	//Construtor para listar participantes
	public ParticipanteDTO(Participante participante) {
		this(participante.getId(), participante.getNomeSocial());
	}

	//Construtor para atualizar participantes
	public ParticipanteDTO(Long id, String nomeSocial) {
		this.id = id; 
		this.nomeSocial = nomeSocial;
	}

}
