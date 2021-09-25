package br.com.ovigia.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ronda {
	public IdRonda id;
	public List<Localizacao> localizacoes;

	public Ronda() {
	}

	public Ronda(IdRonda id) {
		this.id = id;
	}

	public void add(Localizacao localizacao) {
		if (localizacoes == null) {
			localizacoes = new ArrayList<>();
		}
		localizacoes.add(localizacao);
	}

	public String obterIdVigia() {
		if (id == null) {
			return null;
		}
		return id.idVigia;
	}

	public Date obterData() {
		if (id == null) {
			return null;
		}
		return id.data;
	}

}
