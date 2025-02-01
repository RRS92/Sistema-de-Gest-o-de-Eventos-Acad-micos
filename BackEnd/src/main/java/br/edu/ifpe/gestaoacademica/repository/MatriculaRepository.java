package br.edu.ifpe.gestaoacademica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.ifpe.gestaoacademica.entities.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Long>{
	
	 @Query("SELECT m FROM Matricula m JOIN FETCH m.aluno")
	    List<Matricula> findAllWithAluno();
	

}
