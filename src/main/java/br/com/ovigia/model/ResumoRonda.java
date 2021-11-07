package br.com.ovigia.model;

import java.util.Date;

public class ResumoRonda {
	public String idVigia;
	public Date data;
	public Double distancia = 0d;
	public Long totalChamados = 0l;
	public Double tempo = 0d;
	public Character escalaTempo;

	public ResumoRonda() {
	}

	public ResumoRonda(String mensagem) {
		System.out.println(mensagem);
	}

	public ResumoRonda(String mensagem, boolean ok) {
		System.out.println(mensagem);
		this.idVigia = "TESTES";
	}
}
