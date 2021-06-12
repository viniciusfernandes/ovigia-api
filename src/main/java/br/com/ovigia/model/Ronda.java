package br.com.ovigia.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ronda {
	private IdRonda id;
	private List<Localizacao> localizacoes;

	public Ronda() {
	}

	public Ronda(IdRonda id) {
		this.id = id;
	}

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

	public IdRonda getId() {
		return id;
	}

	public void setId(IdRonda id) {
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
