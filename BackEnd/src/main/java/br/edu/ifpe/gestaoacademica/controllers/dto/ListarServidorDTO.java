package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Banco;
import br.edu.ifpe.gestaoacademica.entities.Endereco;
import br.edu.ifpe.gestaoacademica.entities.Servidor;

public record ListarServidorDTO(
		Long id,
		String siape,
		String cargo,
		String nome,
		String cpf,
		String rg,
		String dataNasc,
		String telefone,
		String email,
		Endereco endereco,
		Banco banco) {
	
	public ListarServidorDTO(Servidor servidor) {
		this(servidor.getId(), servidor.getSiape(), servidor.getCargo(), servidor.getNome(), servidor.getCpf(), servidor.getRg(), 
				servidor.getDataNasc(), servidor.getTelefone(), servidor.getEmail(), servidor.getEndereco(), servidor.getBanco());
	}
}
