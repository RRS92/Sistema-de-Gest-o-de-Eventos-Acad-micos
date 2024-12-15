package br.edu.ifpe.gestaoacademica.controllers.dto;

import br.edu.ifpe.gestaoacademica.entities.Utilizador;

public record UtilizadorDTO(
		Long id,
		String login,
	
		String senha
		
		
		) {
	
	//Construtor para cadastrar evento
		public UtilizadorDTO(String login, String senha) {
			this(null, login, senha);
		}

		//Construtor para listar evento
		public UtilizadorDTO(Utilizador utilizador) {
			this(utilizador.getId(), utilizador.getUsername(), utilizador.getPassword());
		}
		
		//Construtor para atualizar evento
		public UtilizadorDTO(Long id, String login,String senha) {
			this.id = id; 
			this.login = login;
			this.senha=senha;
		
		
		}
	
}
