package br.com.ovigia.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ronda {
	public Id id;
	public Date inicio;
	public Date fim;
	public List<Localizacao> localizacoes = new ArrayList<>();

	public Ronda() {
	}

	public Ronda(Id id) {
		this.id = id;
	}

	public void add(Localizacao localizacao) {
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
