package br.edu.ifpe.gestaoacademica.controllers.dto;

import java.util.Base64;

import br.edu.ifpe.gestaoacademica.entities.Utilizador;

public record UtilizadorDTO(
		Long id,
		String login,
	
		String senha
		,
        String fotoBase64 // Representação segura da imagem em texto
		
		) {
	
	//Construtor para cadastrar evento
		public UtilizadorDTO(String login, String senha,String fotoBase64) {
			this(null, login, senha,null);
		}

		//Construtor para listar evento
		public UtilizadorDTO(Utilizador utilizador) {
			this(utilizador.getId(), utilizador.getUsername(), utilizador.getPassword()
					
					,
					utilizador.getFoto() != null ? Base64.getEncoder().encodeToString(utilizador.getFoto()) : null
					
					);
		}
		
		//Construtor para atualizar evento
		public UtilizadorDTO(Long id, String login,String senha, String fotoBase64) {
			this.id = id; 
			this.login = login;
			this.senha=senha;
	        this.fotoBase64 = fotoBase64;

		
		}
	
}
