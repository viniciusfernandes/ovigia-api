package br.com.ovigia.auth.businessrule;

import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Usuario;
import br.com.ovigia.model.enumeration.TipoUsuario;

public class SingInResponse {
	public String id;
	public String token;
	public String nome;
	public String telefone;
	public TipoUsuario tipoUsuario;
	public Localizacao localizacao;

	public SingInResponse(Usuario usuario, String token) {
		id = usuario.id;
		nome = usuario.nome;
		telefone = usuario.formatarTelefone();
		tipoUsuario = usuario.tipoUsuario;
		localizacao = usuario.localizacao;
		this.token = token;
	}

}
