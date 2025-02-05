package br.edu.ifpe.gestaoacademica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifpe.gestaoacademica.entities.Participante;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
	
	List<Participante> findAllByAtivoTrue();
	
	@Query("SELECT p FROM Participante p " +
	           "JOIN FETCH p.aluno a " +
	           "JOIN FETCH p.evento e " +
	           "LEFT JOIN FETCH p.certificado c " +
	           "WHERE e.id = :eventoId")
	    List<Participante> findParticipantesByEvento(@Param("eventoId") Long eventoId);
 

}
