package br.edu.ifpe.gestaoacademica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.gestaoacademica.entities.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
	List<Curso> findAllByAtivoTrue();
}
