package br.edu.ifpe.gestaoacademica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.gestaoacademica.entities.Avaliacao;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
	List<Avaliacao> findAllByAtivoTrue();

}
