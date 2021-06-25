package br.com.ovigia.auth.service;

import org.springframework.stereotype.Service;

import br.com.ovigia.auth.model.Usuario;
import br.com.ovigia.auth.repository.UsuarioRepository;
import reactor.core.publisher.Mono;

/**
 * This is just an example, you can load the user from the database from the
 * repository.
 * 
 */
@Service
public class UserService {

	private UsuarioRepository repository;

	public UserService(UsuarioRepository repository) {
		this.repository = repository;
	}

	public Mono<String> obterPorEmail(String email) {
		return repository.obterPorEmail(email);
	}

	public Mono<Void> criarUsuario(Usuario usuario) {
		return repository.criarUsuario(usuario);
	}
}
