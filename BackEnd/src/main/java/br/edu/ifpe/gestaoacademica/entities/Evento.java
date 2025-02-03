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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Evento")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Evento {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private String data;
    private String local;
    private String tipo;
    private boolean ativo;
    
    @ManyToOne
    @JoinColumn(name = "idServidor")
	@JsonIgnore
    private Servidor servidor;
    
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)
	@JsonIgnore
    private List<Avaliacao> avaliacoes;
    
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
    private List<Participante> participantes;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Transporte> transportes;
    
    @OneToOne(mappedBy = "evento", cascade = CascadeType.ALL)
	@JsonIgnore
    private Certificado certificado;
    
    public void inativar() {
        this.ativo = false;
    }
}
