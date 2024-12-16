package br.edu.ifpe.gestaoacademica.infra;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.edu.ifpe.gestaoacademica.entities.Utilizador;

@Service
public class TokenService {
	@Value("${api.security.token.secret}")
	private String secret;

	public String gerarToken(Utilizador utilizador) {
		try {
			var algorithm = Algorithm.HMAC256(secret);
			return JWT.create().withIssuer("Gestao_academica").withSubject(utilizador.getLogin())
					.withExpiresAt(dataExpiracao())
					// .withClaim("id", utilizador.getId())
					.sign(algorithm);

		} catch (JWTCreationException exception) {
			throw new RuntimeException("Erro ao gerar o token", exception);

		}
	}

	private Instant dataExpiracao() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}

	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm).withIssuer("Gestao_academica").build().verify(token).getSubject();
		} catch (JWTVerificationException exception) {
			return null;
		}
	}
}
