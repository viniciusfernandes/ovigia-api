package br.com.ovigia.auth.businessrule.singon;

import br.com.ovigia.mapper.Parser;
import br.com.ovigia.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.validation.Valid;

@Mapper
public interface SignOnRequestMapper extends Parser<SignOnRequest, Usuario> {
    @Mapping(source = "email", target = "email")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "telefone", target = "telefone")
    @Mapping(source = "tipoUsuario", target = "tipoUsuario")
    @Mapping(source = "localizacao", target = "localizacao")
    @Mapping(source = "password", target = "password")
    Usuario map(@Valid SignOnRequest request);


}
