package br.edu.ifpe.gestaoacademica.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Participante")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Participante {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private boolean ativo;
	
	
	//@ManyToOne
	//@JoinColumn(name = "idUsuario") 
	//private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "idAluno") 
	@JsonIgnore
	private Aluno aluno;
	  
	    
	@ManyToOne
	@JoinColumn(name = "idEvento")
	@JsonIgnore
	private Evento evento;
	
	@ManyToOne
	@JoinColumn(name = "idCertificado")
	@JsonIgnore
	private Certificado certificado;
	
	
	@OneToMany(mappedBy = "participante", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Avaliacao> avaliacoes;
	
	public void inativar() {
		this.ativo = false;
	}
}