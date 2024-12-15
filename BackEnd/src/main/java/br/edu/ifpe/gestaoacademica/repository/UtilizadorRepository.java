package br.edu.ifpe.gestaoacademica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import br.edu.ifpe.gestaoacademica.entities.Utilizador;

@Repository
public interface UtilizadorRepository extends JpaRepository<Utilizador, Long>{

	UserDetails findByLogin(String login);
	List<Utilizador> findAllByAtivoTrue();

}