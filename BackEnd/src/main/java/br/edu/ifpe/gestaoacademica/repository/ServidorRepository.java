package br.edu.ifpe.gestaoacademica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.gestaoacademica.entities.Servidor;

public interface ServidorRepository extends JpaRepository<Servidor, Long> {
	List<Servidor> findAllByAtivoTrue();
}
