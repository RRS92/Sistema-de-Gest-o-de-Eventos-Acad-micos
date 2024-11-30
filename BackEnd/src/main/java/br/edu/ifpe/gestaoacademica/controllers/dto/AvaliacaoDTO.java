package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Avaliacao;
import jakarta.validation.constraints.NotBlank;

public record AvaliacaoDTO(
	    Long id,
	    @NotBlank String nota,
	    @NotBlank String comentario,
	    Long idEvento // Adicionado para capturar o ID do evento
	) {
	    // Construtores ajustados para incluir o idEvento
	    public AvaliacaoDTO(String nota, String comentario, Long idEvento) {
	        this(null, nota, comentario, idEvento);
	    }

	    public AvaliacaoDTO(Avaliacao avaliacao) {
	        this(avaliacao.getId(), avaliacao.getNota(), avaliacao.getComentario(), avaliacao.getEvento() != null ? avaliacao.getEvento().getId() : null);
	    }
	}
