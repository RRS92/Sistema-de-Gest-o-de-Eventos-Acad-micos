package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Banco;
import br.edu.ifpe.gestaoacademica.entities.Endereco;
import br.edu.ifpe.gestaoacademica.entities.Servidor;

public record ServidorDTO(
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
	
	//Construtor para cadastrar servidor
	public ServidorDTO(String siape, String cargo, String nome, String cpf, String rg, String dataNasc, 
			String telefone, String email, Endereco endereco, Banco banco) {
		this(null, siape, cargo, nome, cpf, rg, dataNasc, telefone, email, endereco, banco);
	}
	
	//Construtor para listar servidor
	public ServidorDTO(Servidor servidor) {
		this(servidor.getId(), servidor.getSiape(), servidor.getCargo(), servidor.getNome(), servidor.getCpf(), servidor.getRg(), 
				servidor.getDataNasc(), servidor.getTelefone(), servidor.getEmail(), servidor.getEndereco(), servidor.getBanco());
	}
	
	//Construtor para atualizar servidor
	public ServidorDTO(Long id, String siape, String cargo, String nome, String dataNasc, 
			String telefone, String email, Endereco endereco, Banco banco) {
		this(id, siape, cargo, nome, null, null, dataNasc, telefone, email, endereco, banco);
	}
}
