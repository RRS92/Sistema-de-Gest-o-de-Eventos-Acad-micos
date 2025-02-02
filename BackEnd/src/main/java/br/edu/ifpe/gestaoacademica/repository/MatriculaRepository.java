package br.edu.ifpe.gestaoacademica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifpe.gestaoacademica.entities.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Long>{
	
	@Query("SELECT m FROM Matricula m WHERE m.aluno.id = :idAluno")
    Matricula findByAlunoId(@Param("idAluno") Long idAluno);
	

}
