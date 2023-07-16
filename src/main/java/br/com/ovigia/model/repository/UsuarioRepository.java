package br.com.ovigia.model.repository;

import br.com.ovigia.model.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {

    Mono<Usuario> obterUsuario(String email, String password);

    Mono<Void> criarUsuario(Usuario usuario);

}
