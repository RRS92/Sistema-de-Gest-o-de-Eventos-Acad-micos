package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Certificado;
import br.edu.ifpe.gestaoacademica.entities.Evento;
import br.edu.ifpe.gestaoacademica.entities.Participante;
import br.edu.ifpe.gestaoacademica.entities.Usuario;
import jakarta.validation.constraints.NotBlank;

public record ParticipanteDTO(
		Long id,
		Usuario usuario,
		Evento evento,
		Certificado certificado
		) {

	//Construtor para cadastrar participantes
	public ParticipanteDTO(Usuario usuario,Evento evento,Certificado certificado) {
		this(null, usuario, evento, certificado);
	}

	//Construtor para listar participantes
	public ParticipanteDTO(Participante participante) {
		this(participante.getId(), participante.getUsuario(), participante.getEvento(), participante.getCertificado() );
	}

	//Construtor para atualizar participantes
	public ParticipanteDTO(Long id, Usuario usuario,Evento evento,Certificado certificado) {
		this.id = id;
		this.usuario = usuario;
		this.evento = evento;
		this.certificado = certificado;
		
	}

}
