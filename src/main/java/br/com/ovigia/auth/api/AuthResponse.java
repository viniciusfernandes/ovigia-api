package br.com.ovigia.auth.api;

import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Usuario;
import br.com.ovigia.model.enumeration.TipoUsuario;

public class AuthResponse {
	public String token;
	public String nome;
	public TipoUsuario tipoUsuario;
	public Localizacao localizacao;

	public AuthResponse(Usuario usuario, String token) {
		nome = usuario.nome;
		tipoUsuario = usuario.tipoUsuario;
		localizacao = usuario.localizacao;
		this.token = token;
	}

}
