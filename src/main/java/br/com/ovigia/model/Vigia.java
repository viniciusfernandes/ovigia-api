package br.com.ovigia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Vigia {
	private String id;
	private String nome;
	private String telefone;
	private String email;
	
	@JsonIgnore
	private List<Cliente> clientes;

	public Vigia() {
	}

	public Vigia(String id, String nome) {
		this.id = id;
		this.nome = nome;
	}

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

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

}
