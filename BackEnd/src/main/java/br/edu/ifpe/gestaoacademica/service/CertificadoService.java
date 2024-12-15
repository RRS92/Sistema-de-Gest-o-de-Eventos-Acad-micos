package br.edu.ifpe.gestaoacademica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.edu.ifpe.gestaoacademica.controllers.dto.CertificadoDTO;
import br.edu.ifpe.gestaoacademica.entities.Avaliacao;
import br.edu.ifpe.gestaoacademica.entities.Certificado;
import br.edu.ifpe.gestaoacademica.entities.Evento;
import br.edu.ifpe.gestaoacademica.repository.CertificadoRepository;
import br.edu.ifpe.gestaoacademica.repository.EventoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")

@Service
public class CertificadoService {

	@Autowired
	private CertificadoRepository certificadoRepository;
	
	@Autowired
    private EventoRepository eventoRepository;

	public Certificado cadastrarCertificado(CertificadoDTO dadosCertificadoDTO) {

		Certificado certificado = new Certificado();
		certificado.setCargaHoraria(dadosCertificadoDTO.cargaHoraria());
		certificado.setDescricao(dadosCertificadoDTO.descricao());
		certificado.setAtivo(true);
		
		Evento evento = eventoRepository.findById(dadosCertificadoDTO.idEvento())
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));
        certificado.setEvento(evento);
		
		return certificadoRepository.save(certificado);

	}

	public List<Certificado> listarCertificados(){
		return certificadoRepository.findAllByAtivoTrue();
	}
	
	// Método para listar as avaliações de um evento específico
    public List<Certificado> listarCertificadosPorEvento(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado"));
        return certificadoRepository.findByEvento(evento);
    }

	public Certificado atualizarCertificado(@Valid CertificadoDTO dadosCertificadoDTO) {

		Certificado certificado = certificadoRepository.findById(dadosCertificadoDTO.id())
				.orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado"));

		if (dadosCertificadoDTO.cargaHoraria() != null) certificado.setCargaHoraria(dadosCertificadoDTO.cargaHoraria());
		if (dadosCertificadoDTO.descricao() != null) certificado.setDescricao(dadosCertificadoDTO.descricao());

		return certificadoRepository.save(certificado);
	}
	
	public void inativarCertificado(Long id) {
		Certificado certificado = certificadoRepository.getReferenceById(id);
		certificado.inativar();
		certificadoRepository.save(certificado);
	}

	public void deletarCertificado(Long id) {
		certificadoRepository.deleteById(id);
	}
}
