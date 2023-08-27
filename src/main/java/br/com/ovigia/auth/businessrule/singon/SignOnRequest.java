package br.com.ovigia.auth.businessrule.singon;

import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.enumeration.TipoUsuario;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SignOnRequest {
    @NotBlank
    public String email;
    @NotBlank
    public String nome;
    public String telefone;
    @NotNull
    public TipoUsuario tipoUsuario;
    public Localizacao localizacao;
    @NotBlank
    public String password;

}
