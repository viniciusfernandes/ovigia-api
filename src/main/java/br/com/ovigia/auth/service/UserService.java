package br.com.ovigia.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ovigia.auth.model.Usuario;
import br.com.ovigia.auth.repository.UsuarioRepository;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	private UsuarioRepository repository;

	private PasswordEncoder passwordEncoder;

	public UserService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	public Mono<Boolean> isUsuarioExistente(String email, String password) {
		password = passwordEncoder.encode(password);
		return repository.isUsuarioExistente(email, password);
	}

	public Mono<Void> criarUsuario(Usuario usuario) {
		var password = passwordEncoder.encode(usuario.getPassword());
		usuario.setPassword(password);
		return repository.criarUsuario(usuario);
	}
}