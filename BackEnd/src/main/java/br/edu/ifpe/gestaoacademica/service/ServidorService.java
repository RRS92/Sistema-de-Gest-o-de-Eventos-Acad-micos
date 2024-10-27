package br.edu.ifpe.gestaoacademica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.edu.ifpe.gestaoacademica.controllers.dto.AtualizarServidorDTO;
import br.edu.ifpe.gestaoacademica.controllers.dto.CadastrarServidorDTO;
import br.edu.ifpe.gestaoacademica.entities.Servidor;
import br.edu.ifpe.gestaoacademica.repository.ServidorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")

@Service
public class ServidorService {

	@Autowired
	private ServidorRepository servidorRepository;


	public Servidor cadastrarServidor(CadastrarServidorDTO dadosServidorDTO) {
		
		Servidor servidor = new Servidor();
		
		servidor.setSiape(dadosServidorDTO.siape());
		servidor.setCargo(dadosServidorDTO.cargo());
		servidor.setAtivo(true);

		return servidorRepository.save(servidor);

	}

	public List<Servidor> listarServidores() {
		return servidorRepository.findAllByAtivoTrue();
	}

	public Servidor atualizarServidor(@Valid AtualizarServidorDTO dadosServidorDTO) {
		
	    Servidor servidor = servidorRepository.findById(dadosServidorDTO.id())
	            .orElseThrow(() -> new EntityNotFoundException("Servidor não encontrado"));

		if (dadosServidorDTO.siape() != null) {
			servidor.setSiape(dadosServidorDTO.siape());
		}

		if (dadosServidorDTO.cargo() != null) {
			servidor.setCargo(dadosServidorDTO.cargo());
		}
		
		return servidorRepository.save(servidor);
	}

	public void inativarServidor(Long id) {
		Servidor servidor = servidorRepository.getReferenceById(id);
		servidor.inativar();
		servidorRepository.save(servidor);
	}

	public void deletarServidor(Long id) {
		servidorRepository.deleteById(id);
	}
}
