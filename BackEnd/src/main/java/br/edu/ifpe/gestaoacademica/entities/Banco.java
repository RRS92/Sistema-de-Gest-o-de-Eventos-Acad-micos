package br.edu.ifpe.gestaoacademica.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Banco")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Banco {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nomeBanco;
	private String numConta;
	private String agencia;
	private String operacao;
	private boolean ativo;
	
	@OneToMany(mappedBy = "banco")
	private List<Usuario> usuarios;
	
	public void inativar() {
		this.ativo = false;
	}
}
