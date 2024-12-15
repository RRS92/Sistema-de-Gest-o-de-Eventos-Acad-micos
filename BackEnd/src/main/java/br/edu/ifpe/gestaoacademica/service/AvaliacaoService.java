package br.edu.ifpe.gestaoacademica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.edu.ifpe.gestaoacademica.controllers.dto.AvaliacaoDTO;
import br.edu.ifpe.gestaoacademica.entities.Avaliacao;
import br.edu.ifpe.gestaoacademica.entities.Evento;
import br.edu.ifpe.gestaoacademica.repository.AvaliacaoRepository;
import br.edu.ifpe.gestaoacademica.repository.EventoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private EventoRepository eventoRepository;

    public Avaliacao cadastrarAvaliacao(AvaliacaoDTO dadosAvaliacaoDTO) {
        

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(dadosAvaliacaoDTO.nota());
        avaliacao.setComentario(dadosAvaliacaoDTO.comentario());
        avaliacao.setAtivo(true);

        Evento evento = eventoRepository.findById(dadosAvaliacaoDTO.idEvento())
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));
        avaliacao.setEvento(evento);
        avaliacao.setParticipante(dadosAvaliacaoDTO.participante());

        return avaliacaoRepository.save(avaliacao);
    }

    public List<Avaliacao> listarAvaliacao() {
        return avaliacaoRepository.findAllByAtivoTrue();
    }

    // Método para listar as avaliações de um evento específico
    public List<Avaliacao> listarAvaliacoesPorEvento(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));
        return avaliacaoRepository.findByEvento(evento);
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
