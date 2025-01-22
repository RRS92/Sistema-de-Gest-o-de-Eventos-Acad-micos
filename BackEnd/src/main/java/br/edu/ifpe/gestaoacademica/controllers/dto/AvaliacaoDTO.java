package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Avaliacao;
import br.edu.ifpe.gestaoacademica.entities.Participante;
import jakarta.validation.constraints.NotBlank;

public record AvaliacaoDTO(
	    Long id,
	    @NotBlank 
	    String nota,
	    @NotBlank 
	    String comentario,
	    Long idEvento,
	    Participante participante) {
	
	    // Construtores ajustados para incluir o idEvento
	    public AvaliacaoDTO(String nota, String comentario, Long idEvento, Participante participante) {
	        this(null, nota, comentario, idEvento, participante);
	    }

	    public AvaliacaoDTO(Avaliacao avaliacao) {
	        this(avaliacao.getId(), avaliacao.getNota(), avaliacao.getComentario(), 
	        		avaliacao.getEvento() != null ? avaliacao.getEvento().getId() : null, avaliacao.getParticipante());
	    }
	    
	  //Construtor para atualizar transporte
		public AvaliacaoDTO(Long id, String nota, String comentario, Long idEvento, Participante participante) {
			this.id = id; 
			this.nota = nota;
			this.comentario = comentario;
			this.idEvento = idEvento;
			this.participante = participante;
		}
	}
