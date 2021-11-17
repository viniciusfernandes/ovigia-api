package br.com.ovigia.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ovigia.model.enumeration.TipoSituacaoRonda;

public class Ronda {
	public IdRonda id;
	public Date inicio;
	public Date fim;
	public TipoSituacaoRonda situacao;
	public List<Localizacao> localizacoes = new ArrayList<>();

	public Ronda() {
	}

	public Ronda(IdRonda id) {
		this.id = id;
	}

	public void add(Localizacao localizacao) {
		localizacoes.add(localizacao);
	}

	public void add(List<Localizacao> localizacoes) {
		this.localizacoes.addAll(localizacoes);
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
