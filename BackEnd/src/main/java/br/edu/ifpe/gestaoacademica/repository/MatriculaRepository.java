package br.edu.ifpe.gestaoacademica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.gestaoacademica.entities.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Long>{
	List<Matricula> findAllByAtivoTrue();
}
