package br.edu.ifpe.gestaoacademica.infra;

import br.edu.ifpe.gestaoacademica.entities.enums.AcessLevel;

public record DadosTokenJWTdto(String token, Long id, Long idUser, AcessLevel acessLevel) {

	
}
