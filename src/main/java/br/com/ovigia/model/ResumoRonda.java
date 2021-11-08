package br.com.ovigia.model;

import java.util.Date;

import br.com.ovigia.businessrule.util.DataUtil;

public class ResumoRonda {
	public String idVigia;
	public Date data;
	public Double distancia = 0d;
	public Long totalChamados = 0l;
	public Double tempo = 0d;
	public Character escalaTempo;

	public ResumoRonda() {
	}

	public ResumoRonda init() {
		distancia = 0d;
		totalChamados = 0l;
		tempo = 0d;
		data = DataUtil.ajustarData();
		return this;
	}
}
