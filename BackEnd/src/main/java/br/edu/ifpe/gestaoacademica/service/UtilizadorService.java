package br.edu.ifpe.gestaoacademica.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ifpe.gestaoacademica.controllers.dto.UtilizadorDTO;
import br.edu.ifpe.gestaoacademica.entities.Utilizador;
import br.edu.ifpe.gestaoacademica.entities.enums.AcessLevel;
import br.edu.ifpe.gestaoacademica.repository.UtilizadorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;


@Service
public class UtilizadorService implements UserDetailsService {
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	UtilizadorRepository utilizadorRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return utilizadorRepository.findByLogin(username);
	}
	
	
	

	public Utilizador cadastrarUtilizadorAluno(UtilizadorDTO dadosUtilizadorDTO) {
	    
		String senhaCriptografada = passwordEncoder.encode(dadosUtilizadorDTO.senha());
		
	    Utilizador utilizador = new Utilizador();
	    utilizador.setLogin(dadosUtilizadorDTO.login());
	    
	    utilizador.setSenha(senhaCriptografada);
	    utilizador.setAtivo(true);
	    utilizador.setAcessLevel(AcessLevel.ALUNO);
	    
	    return utilizadorRepository.save(utilizador);

	}
	
public Utilizador cadastrarUtilizadorServidor(UtilizadorDTO dadosUtilizadorDTO) {
	    
		String senhaCriptografada = passwordEncoder.encode(dadosUtilizadorDTO.senha());
		
	    Utilizador utilizador = new Utilizador();
	    utilizador.setLogin(dadosUtilizadorDTO.login());
	    
	    utilizador.setSenha(senhaCriptografada);
	    utilizador.setAtivo(true);
	    utilizador.setAcessLevel(AcessLevel.SERVIDOR);
	    
	    return utilizadorRepository.save(utilizador);

	}

	public List<Utilizador> listarUtilizador() {
	    return utilizadorRepository.findAllByAtivoTrue();
	}

	public Utilizador atualizarUtilizador(@Valid UtilizadorDTO dadosUtilizadorDTO) {
	    
	    Utilizador utilizador = utilizadorRepository.findById(dadosUtilizadorDTO.id())
	            .orElseThrow(() -> new EntityNotFoundException("Utilizador não encontrado"));
	    
	    if (dadosUtilizadorDTO.login() != null) {
	    	utilizador.setLogin(dadosUtilizadorDTO.login());}


	    if (dadosUtilizadorDTO.senha() != null) {
			String senhaCriptografadaAtualizada = passwordEncoder.encode(dadosUtilizadorDTO.senha());

	    	utilizador.setSenha(senhaCriptografadaAtualizada);}

	    
	    return utilizadorRepository.save(utilizador);
	}

	public void inativarUtilizador(Long id) {
	    Utilizador utilizador = utilizadorRepository.getReferenceById(id);
	    utilizador.inativar();
	    utilizadorRepository.save(utilizador);
	}

	public void deletarUtilizador(Long id) {
	    utilizadorRepository.deleteById(id);
	}


	
}
