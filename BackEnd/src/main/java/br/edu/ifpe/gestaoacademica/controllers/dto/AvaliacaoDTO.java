package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Avaliacao;
import br.edu.ifpe.gestaoacademica.entities.Evento;
import br.edu.ifpe.gestaoacademica.entities.Participante;
import jakarta.validation.constraints.NotBlank;

public record AvaliacaoDTO(
	    Long id,
	    @NotBlank 
	    String nota,
	    @NotBlank 
	    String comentario,
	    
	    Evento evento,
	    Participante participante) {
	
	    // Construtores ajustados para incluir o idEvento
	    public AvaliacaoDTO(String nota, String comentario, Evento evento, Participante participante) {
	        this(null, nota, comentario, evento, participante);
	    }

	    public AvaliacaoDTO(Avaliacao avaliacao) {
	        this(avaliacao.getId(), avaliacao.getNota(), avaliacao.getComentario(), 
	        		avaliacao.getEvento(), avaliacao.getParticipante());
	    }
	    
	  //Construtor para atualizar transporte
		public AvaliacaoDTO(Long id, String nota, String comentario, Evento evento, Participante participante) {
			this.id = id; 
			this.nota = nota;
			this.comentario = comentario;
			this.evento = evento;
			this.participante = participante;
		}
	}
