package br.edu.ifpe.gestaoacademica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.edu.ifpe.gestaoacademica.controllers.dto.AtualizarEventoDTO;
import br.edu.ifpe.gestaoacademica.controllers.dto.CadastrarEventoDTO;
import br.edu.ifpe.gestaoacademica.entities.Evento;
import br.edu.ifpe.gestaoacademica.repository.EventoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")

@Service
public class EventoService {

	@Autowired
	private EventoRepository eventoRepository;

	public Evento cadastrarEvento(CadastrarEventoDTO dadosEventoDTO) {

		Long idServidorFixo = 1L;

		Evento evento = new Evento();
		evento.setNome(dadosEventoDTO.nome());
		evento.setDescricao(dadosEventoDTO.descricao());
		evento.setData(dadosEventoDTO.data());
		evento.setLocal(dadosEventoDTO.local());
		evento.setTipo(dadosEventoDTO.tipo());
		evento.setAtivo(true);

		evento.setId_servidor(idServidorFixo);
		return eventoRepository.save(evento);

	}

	public List<Evento> listarEventos() {
		return eventoRepository.findAllByAtivoTrue();
	}
	
	public Evento atualizarEvento(@Valid AtualizarEventoDTO dadosEventoDTO) {
		
	    Evento evento = eventoRepository.findById(dadosEventoDTO.id())
	            .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

		if (dadosEventoDTO.nome() != null) {
			evento.setNome(dadosEventoDTO.nome());
		}
		
		if (dadosEventoDTO.descricao() != null) {
			evento.setDescricao(dadosEventoDTO.descricao());
		}
		
		if (dadosEventoDTO.data() != null) {
			evento.setData(dadosEventoDTO.data());
		}
		
		if (dadosEventoDTO.local() != null) {
			evento.setLocal(dadosEventoDTO.local());
		}
		
		if (dadosEventoDTO.tipo() != null) {
			evento.setTipo(dadosEventoDTO.tipo());
		}
		
		return eventoRepository.save(evento);
	}

	public void inativarEvento(Long id) {
		Evento evento = eventoRepository.getReferenceById(id);
		evento.inativar();
		eventoRepository.save(evento);
	}

	public void deletarEvento(Long id) {
        eventoRepository.deleteById(id);
	}
}
