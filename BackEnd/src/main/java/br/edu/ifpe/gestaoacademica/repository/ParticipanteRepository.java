package br.edu.ifpe.gestaoacademica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.gestaoacademica.entities.Participante;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
	List<Participante> findAllByAtivoTrue();

}
