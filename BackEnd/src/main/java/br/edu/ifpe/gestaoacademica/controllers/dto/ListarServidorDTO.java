package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Servidor;

public record ListarServidorDTO(
		Long id,
		String siape,
		String cargo) {
	
	public ListarServidorDTO(Servidor servidor) {
		this(servidor.getId(), servidor.getSiape(), servidor.getCargo());
	}
}
