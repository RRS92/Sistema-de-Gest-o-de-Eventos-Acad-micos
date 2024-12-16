package br.edu.ifpe.gestaoacademica.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "Aluno")
@Entity
@Getter
@Setter

public class Aluno extends Usuario {
	private String matricula;
	private boolean ativo;
	
	public void inativar() {
		this.ativo = false;
	}
	
	@OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matricula> matriculas;
}
