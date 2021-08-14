package br.com.ovigia.auth.businessrule;

import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Usuario;
import br.com.ovigia.model.enumeration.TipoUsuario;

public class UsuarioAutenticado {
	public String token;
	public String nome;
	public TipoUsuario tipoUsuario;
	public Localizacao localizacao;

	public UsuarioAutenticado(Usuario usuario, String token) {
		nome = usuario.nome;
		tipoUsuario = usuario.tipoUsuario;
		localizacao = usuario.localizacao;
		this.token = token;
	}

}
