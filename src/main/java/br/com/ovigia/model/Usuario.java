package br.com.ovigia.model;

import br.com.ovigia.model.enumeration.TipoUsuario;

public class Usuario {
	// ID FIELD
	public String id;
	public String email;
	public String nome;
	public String telefone;
	public TipoUsuario tipoUsuario;
	public Localizacao localizacao;
	public String password;

	public Usuario() {

	}

	public Usuario(String email, String nome) {
		this.email = email;
		this.nome = nome;
	}

}
