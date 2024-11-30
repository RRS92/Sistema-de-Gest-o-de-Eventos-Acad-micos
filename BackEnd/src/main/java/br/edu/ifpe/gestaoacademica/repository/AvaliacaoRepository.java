package br.edu.ifpe.gestaoacademica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.gestaoacademica.entities.Avaliacao;
import br.edu.ifpe.gestaoacademica.entities.Evento;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
	
	List<Avaliacao> findAllByAtivoTrue();

    // Método para buscar avaliações por evento
    List<Avaliacao> findByEvento(Evento evento);
}
