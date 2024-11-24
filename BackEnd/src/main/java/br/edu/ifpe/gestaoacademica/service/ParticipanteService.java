package br.edu.ifpe.gestaoacademica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.edu.ifpe.gestaoacademica.controllers.dto.ParticipanteDTO;
import br.edu.ifpe.gestaoacademica.entities.Participante;
import br.edu.ifpe.gestaoacademica.repository.ParticipanteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")

@Service
public class ParticipanteService {
	
	@Autowired
	private ParticipanteRepository participanteRepository;
	
	public Participante cadastrarParticipante(ParticipanteDTO dadosParticipanteDTO) {

		Long idUsuarioFixo = 1L;
		Long idEventoFixo = 1L;
		Long idCertificadoFixo = 1L;

		Participante participante = new Participante();
		participante.setNomeSocial(dadosParticipanteDTO.nomeSocial());
		participante.setAtivo(true);

		participante.setIdUsuario(idUsuarioFixo);
		participante.setIdEvento(idEventoFixo);
		participante.setIdCertificado(idCertificadoFixo);

		return participanteRepository.save(participante);
	}
	
	public List<Participante> listarParticipante() {
		return participanteRepository.findAllByAtivoTrue();
	}
	
	public Participante atualizarParticipante(@Valid ParticipanteDTO dadosParticipanteDTO) {

		Participante participante = participanteRepository.findById(dadosParticipanteDTO.id())
				.orElseThrow(() -> new EntityNotFoundException("Participante não encontrado"));

		if (dadosParticipanteDTO.nomeSocial() != null) 	    participante.setNomeSocial(dadosParticipanteDTO.nomeSocial());


		return participanteRepository.save(participante);
	}
	
	public void inativarParticipante(Long id) {
		Participante participante = participanteRepository.getReferenceById(id);
		participante.inativar();
		participanteRepository.save(participante);
	}
	
	public void deletarParticipante(Long id) {
		participanteRepository.deleteById(id);
	}

}
