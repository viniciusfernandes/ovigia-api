package br.com.ovigia.repository.impl.hash;

import br.com.ovigia.model.Usuario;
import br.com.ovigia.model.repository.UsuarioRepository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UsuarioHashRepository implements UsuarioRepository {

    private final Map<String, Usuario> table = new HashMap<>();

    public Mono<Usuario> obterUsuario(String email, String password) {
        for (var e : table.values()) {
            if (email.equals(e.email) && password.equals(e.password)) {
                return Mono.just(e);
            }
        }
        return Mono.empty();
    }

    public Mono<Void> criarUsuario(Usuario usuario) {
        usuario.id = UUID.randomUUID().toString();
        table.put(usuario.id, usuario);
        return Mono.empty();
    }

}
