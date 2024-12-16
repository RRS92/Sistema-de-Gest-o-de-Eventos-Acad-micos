package br.edu.ifpe.gestaoacademica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.gestaoacademica.entities.Certificado;
import br.edu.ifpe.gestaoacademica.entities.Evento;

public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
	List<Certificado> findAllByAtivoTrue();
	
	// Método para buscar certificados por evento
    List<Certificado> findByEvento(Evento evento);
}
