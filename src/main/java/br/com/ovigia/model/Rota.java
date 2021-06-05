package br.com.ovigia.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Rota {
	private IdRota id;
	private List<Localizacao> localizacoes;

	public List<Localizacao> getLocalizacoes() {
		return localizacoes;
	}

	public void setLocalizacoes(List<Localizacao> localizacoes) {
		this.localizacoes = localizacoes;
	}

	public void add(Localizacao localizacao) {
		if (localizacoes == null) {
			localizacoes = new ArrayList<>();
		}
		localizacoes.add(localizacao);
	}

	public IdRota getId() {
		return id;
	}

	public void setId(IdRota id) {
		this.id = id;
	}

	public String obterIdVigia() {
		if (id == null) {
			return null;
		}
		return id.getIdVigia();
	}

	public Date obterData() {
		if (id == null) {
			return null;
		}
		return id.getData();
	}

}
