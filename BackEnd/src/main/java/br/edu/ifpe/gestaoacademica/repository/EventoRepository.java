package br.edu.ifpe.gestaoacademica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.gestaoacademica.entities.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long>{

}
