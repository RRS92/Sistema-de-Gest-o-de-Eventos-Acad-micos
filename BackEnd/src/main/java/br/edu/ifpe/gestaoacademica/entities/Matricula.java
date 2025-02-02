package br.edu.ifpe.gestaoacademica.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Matricula")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Matricula {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numMatricula;
    private String periodoIngresso;
    private String turno;
    private String nomeCurso;
    private String modalidade;
    private boolean ativo;

    @OneToOne
    @JoinColumn(name = "idAluno", unique = true)
    private Aluno aluno;

   

    public void inativar() {
        this.ativo = false;
    }
}

