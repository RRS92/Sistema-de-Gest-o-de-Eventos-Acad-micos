package br.edu.ifpe.gestaoacademica.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Avaliacao")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Avaliacao {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nota;
	private String comentario;
	private boolean ativo;
	
	@ManyToOne
	@JoinColumn(name = "id_evento")
	@JsonIgnore
	private Evento evento;
	
	@ManyToOne
	@JoinColumn(name = "id_participante")
	@JsonIgnore
	private Participante participante;
	
	public void inativar() {
		this.ativo = false;
	}
}
