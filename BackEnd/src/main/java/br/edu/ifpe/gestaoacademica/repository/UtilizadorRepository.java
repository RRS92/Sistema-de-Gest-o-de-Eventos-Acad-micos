package br.edu.ifpe.gestaoacademica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.edu.ifpe.gestaoacademica.entities.Utilizador;

public interface UtilizadorRepository extends JpaRepository<Utilizador, Long>{

	UserDetails findByLogin(String login);

}