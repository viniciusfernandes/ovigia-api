package br.com.ovigia.auth.businessrule;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.ovigia.auth.api.AuthRequest;
import br.com.ovigia.auth.api.AuthResponse;
import br.com.ovigia.auth.repository.UsuarioRepository;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import reactor.core.publisher.Mono;

public class SingInRule implements BusinessRule<AuthRequest, AuthResponse> {
	private UsuarioRepository repository;
	private PasswordEncoder passwordEncoder;
	private JwtUtil jwtUtil;

	public SingInRule(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Mono<Response<AuthResponse>> apply(AuthRequest t) {
		var password = passwordEncoder.encode(t.getPassword());
		return repository.obterUsuario(t.getEmail(), password).map(usuario -> {
			var token = jwtUtil.generateToken(t.getEmail());
			return Response.ok(new AuthResponse(usuario, token));
		}).switchIfEmpty(Mono.just(Response.unautorized()));
	}

}