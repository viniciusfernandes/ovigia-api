package br.com.ovigia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Cliente {
	private String id;
	private String nome;
	private String telefone;
	private String email;
	private List<Vigia> vigias;
	private Localizacao localizacao;
	
	
	@JsonIgnore
	private String idVigia;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Vigia> getVigias() {
		return vigias;
	}

	public void setVigias(List<Vigia> vigias) {
		this.vigias = vigias;
	}

	public Localizacao getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(Localizacao localizacao) {
		this.localizacao = localizacao;
	}

	public String getIdVigia() {
		return idVigia;
	}

	public void setIdVigia(String idVigia) {
		this.idVigia = idVigia;
	}
}
