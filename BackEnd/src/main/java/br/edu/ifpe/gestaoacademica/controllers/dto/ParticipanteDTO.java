package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Certificado;
import br.edu.ifpe.gestaoacademica.entities.Evento;
import br.edu.ifpe.gestaoacademica.entities.Participante;
import br.edu.ifpe.gestaoacademica.entities.Aluno;

public record ParticipanteDTO(
		Long id,
		Aluno aluno,
		Evento evento,
		Certificado certificado
		) {

	//Construtor para cadastrar participantes
	public ParticipanteDTO(Aluno usuario,Evento evento,Certificado certificado) {
		this(null, usuario, evento, certificado);
	}

	//Construtor para listar participantes
	public ParticipanteDTO(Participante participante) {
		this(participante.getId(), participante.getAluno(), participante.getEvento(), participante.getCertificado() );
	}

	//Construtor para atualizar participantes
	public ParticipanteDTO(Long id, Aluno aluno,Evento evento,Certificado certificado) {
		this.id = id;
		this.aluno = aluno;
		this.evento = evento;
		this.certificado = certificado;
		
	}

}
