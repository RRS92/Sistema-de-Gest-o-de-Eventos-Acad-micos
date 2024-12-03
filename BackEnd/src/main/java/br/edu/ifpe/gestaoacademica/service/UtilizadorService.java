package br.edu.ifpe.gestaoacademica.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ifpe.gestaoacademica.repository.UtilizadorRepository;


@Service
public class UtilizadorService implements UserDetailsService {

	
	@Autowired
	UtilizadorRepository utilizadorRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return utilizadorRepository.findByLogin(username);
	}
}
