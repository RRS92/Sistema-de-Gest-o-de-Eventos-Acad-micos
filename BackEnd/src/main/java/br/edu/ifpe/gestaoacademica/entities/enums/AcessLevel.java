package br.edu.ifpe.gestaoacademica.entities.enums;

public enum AcessLevel {
	
	ALUNO ("ALUNO"),
	SERVIDOR ("SERVIDOR");
	public final String level;
	
	AcessLevel(String level) {
		this.level = level;
	}
}
