package br.edu.ifpe.gestaoacademica.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.edu.ifpe.gestaoacademica.controllers.dto.ParticipanteDTO;
import br.edu.ifpe.gestaoacademica.entities.Participante;
import br.edu.ifpe.gestaoacademica.repository.ParticipanteRepository;
import jakarta.persistence.EntityNotFoundException;

@CrossOrigin(origins = "*")

@Service
public class ParticipanteService {
		
	@Autowired
	private ParticipanteRepository participanteRepository;
	
	public Participante cadastrarParticipante(ParticipanteDTO dadosParticipanteDTO) {

		
		Participante participante = new Participante();
		participante.setAtivo(true);

		//participante.setUsuario(dadosParticipanteDTO.usuario());
		participante.setAluno(dadosParticipanteDTO.aluno());
		participante.setEvento(dadosParticipanteDTO.evento());
		participante.setCertificado(dadosParticipanteDTO.certificado());

		return participanteRepository.save(participante);
	}
	
	public List<ParticipanteDTO> listarParticipantesPorEvento(Long eventoId) {
        List<Participante> participantes = participanteRepository.findParticipantesByEvento(eventoId);

        return participantes.stream()
            .map(ParticipanteDTO::new)
            .collect(Collectors.toList());
    }
	
	public List<Participante> listarParticipante() {
		return participanteRepository.findAllByAtivoTrue();
	}
	
	/*public Participante atualizarParticipante(@Valid ParticipanteDTO dadosParticipanteDTO) {

		Participante participante = participanteRepository.findById(dadosParticipanteDTO.id())
				.orElseThrow(() -> new EntityNotFoundException("Participante não encontrado"));

		if (dadosParticipanteDTO.nomeSocial() != null) 	    participante.setNomeSocial(dadosParticipanteDTO.nomeSocial());


		return participanteRepository.save(participante);
	}*/
	
	public void inativarParticipante(Long id) {
		Participante participante = participanteRepository.getReferenceById(id);
		participante.inativar();
		participanteRepository.save(participante);
	}
	
	public void deletarParticipante(Long id) {
	    if (!participanteRepository.existsById(id)) {
	        throw new EntityNotFoundException("Participante não encontrado");
	    }
	    participanteRepository.deleteById(id);
	}


}
