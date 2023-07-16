package br.com.ovigia.model;

import br.com.ovigia.model.enumeration.TipoUsuario;

import java.util.Date;

public class Usuario {
	// ID FIELD
	public String id;
	public String email;
	public String nome;
	public String telefone;
	public TipoUsuario tipoUsuario;
	public Localizacao localizacao;
	public String password;
	public Date dataInicio;

	public Usuario() {

	}

	public Usuario(String email, String nome) {
		this.email = email;
		this.nome = nome;
	}

	public String formatarTelefone() {
		if (telefone == null) {
			return null;
		}
		if (telefone.length() > 11) {
			var tel = telefone.replaceAll("\\D", "");
			if (tel.length() <= 11) {
				return tel;
			}
			var index = tel.length() - 11;
			return tel.substring(index);
		}
		return telefone;
	}

	public boolean isVigia() {
		return tipoUsuario != null && tipoUsuario == TipoUsuario.VIGIA;
	}
}
