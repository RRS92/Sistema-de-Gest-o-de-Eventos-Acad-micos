package br.edu.ifpe.gestaoacademica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.edu.ifpe.gestaoacademica.controllers.dto.AvaliacaoDTO;
import br.edu.ifpe.gestaoacademica.entities.Avaliacao;
import br.edu.ifpe.gestaoacademica.repository.AvaliacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")

@Service
public class AvaliacaoService {

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	public Avaliacao cadastrarAvaliacao(AvaliacaoDTO dadosAvaliacaoDTO) {

		Long idEventoFixo = 1L;
		Long idParticipanteFixo = 1L;

		Avaliacao avaliacao = new Avaliacao();
		avaliacao.setNota(dadosAvaliacaoDTO.nota());
		avaliacao.setComentario(dadosAvaliacaoDTO.comentario());
		avaliacao.setAtivo(true);

		avaliacao.setIdEvento(idEventoFixo);
		avaliacao.setIdParticipante(idParticipanteFixo);

		return avaliacaoRepository.save(avaliacao);
	}

	public List<Avaliacao> listarAvaliacao() {
		return avaliacaoRepository.findAllByAtivoTrue();
	}

	public Avaliacao atualizarAvaliacao(@Valid AvaliacaoDTO dadosAvaliacaoDTO) {

		Avaliacao avaliacao = avaliacaoRepository.findById(dadosAvaliacaoDTO.id())
				.orElseThrow(() -> new EntityNotFoundException("Avaliacão não encontrado"));

		if (dadosAvaliacaoDTO.nota() != null) 	    avaliacao.setNota(dadosAvaliacaoDTO.nota());
		if (dadosAvaliacaoDTO.comentario() != null) avaliacao.setComentario(dadosAvaliacaoDTO.comentario());

		return avaliacaoRepository.save(avaliacao);
	}
	
	public void inativarAvaliacao(Long id) {
		Avaliacao avaliacao = avaliacaoRepository.getReferenceById(id);
		avaliacao.inativar();
		avaliacaoRepository.save(avaliacao);
	}
	
	public void deletarAvaliacao(Long id) {
		avaliacaoRepository.deleteById(id);
	}

}
