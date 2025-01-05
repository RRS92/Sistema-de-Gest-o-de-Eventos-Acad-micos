package br.edu.ifpe.gestaoacademica.controllers.dto;

import java.util.List;

import br.edu.ifpe.gestaoacademica.entities.Evento;
import br.edu.ifpe.gestaoacademica.entities.Servidor;
import br.edu.ifpe.gestaoacademica.entities.Transporte;
import jakarta.validation.constraints.NotBlank;

public record TransporteDTO(
		Long id,
		@NotBlank
		String categoria,
		@NotBlank
		String placa,
		@NotBlank
		String quilometragem,
		@NotBlank
		String nomeMotorista,
		@NotBlank
		String horaSaida,
		@NotBlank
		String horaChegada,
		Evento evento,
		List<Servidor> servidor) {
	
	//Construtor para cadastrar transporte
	public TransporteDTO(String categoria, String placa, String quilometragem, String nomeMotorista, 
						 String horaSaida, String horaChegada, Evento evento, List<Servidor> servidor) {
		this(null, categoria, placa, quilometragem, nomeMotorista, horaSaida, horaChegada, evento, servidor);
	}

	//Construtor para listar transporte
	public TransporteDTO(Transporte transporte) {
		this(transporte.getId(), transporte.getCategoria(), transporte.getPlaca(), transporte.getQuilometragem(), 
			 transporte.getNomeMotorista(), transporte.getHoraSaida(), transporte.getHoraChegada(), 
			transporte.getEvento(),transporte.getServidores());
	}
	
	//Construtor para atualizar transporte
	public TransporteDTO(Long id, String categoria, String placa, String quilometragem, String nomeMotorista, 
						 String horaSaida, String horaChegada, Evento evento, List<Servidor> servidor) {
		this.id = id; 
		this.categoria = categoria;
		this.placa = placa;
		this.quilometragem = quilometragem;
		this.nomeMotorista = nomeMotorista;
		this.horaSaida = horaSaida;
		this.horaChegada = horaChegada;
		this.evento = evento;
		this.servidor = servidor;
	}
}
