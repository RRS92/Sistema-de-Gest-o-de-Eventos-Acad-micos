package br.edu.ifpe.gestaoacademica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.gestaoacademica.entities.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
	List<Aluno> findAllByAtivoTrue();
}
