package br.com.ovigia.model;

import br.com.ovigia.businessrule.util.DataUtil;

public class ResumoRonda {
	public IdRonda id;
	public Double distancia = 0d;
	public Long totalChamados = 0l;
	public Long tempo = 0l;

	public ResumoRonda() {
	}

	public ResumoRonda(String idvigia) {
		id = new IdRonda(idvigia, DataUtil.ajustarData());
	}
}
