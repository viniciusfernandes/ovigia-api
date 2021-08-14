package br.com.ovigia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Cliente extends Usuario {
	public List<String> vigias;

	@JsonIgnore
	private String idVigia;

	public boolean hasVigias() {
		return vigias != null && !vigias.isEmpty();
	}
}
