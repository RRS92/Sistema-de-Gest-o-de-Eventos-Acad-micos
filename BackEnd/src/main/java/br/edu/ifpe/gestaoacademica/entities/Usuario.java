package br.edu.ifpe.gestaoacademica.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Usuario")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String cpf;
	private String rg;
	private String dataNasc;
	private String telefone;
	private String email;
	
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "idEndereco")
	private Endereco endereco;
	
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "idBanco")
	private Banco banco;
	
	@OneToOne
	@JoinColumn(name = "idUtilizador") //pao
    private Utilizador utilizador;
	
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Participante> participante;
	

}
