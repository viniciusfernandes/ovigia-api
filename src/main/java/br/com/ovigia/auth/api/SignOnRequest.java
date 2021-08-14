package br.com.ovigia.auth.api;

import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.enumeration.TipoUsuario;

/**
 *
 * @author ard333
 */
public class SignOnRequest {
	public String email;
	public String nome;
	public String telefone;
	public TipoUsuario tipoUsuario;
	public Localizacao localizacao;
	public String password;

}
