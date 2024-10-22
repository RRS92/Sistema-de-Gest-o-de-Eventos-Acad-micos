package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Evento;

public record ListaEventoDTO(
		Long id,
		String nome,
		String descricao,
		String data,
		String local,
		String tipo
		
		) {

	public ListaEventoDTO(Evento evento) {
		this(
				evento.getId(),
				evento.getNome(), 
				evento.getDescricao(),
				evento.getData(), 
				evento.getLocal(),
				evento.getTipo());
	}

}
