package br.com.ovigia.auth.businessrule;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.ovigia.auth.api.AuthRequest;
import br.com.ovigia.auth.repository.UsuarioRepository;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import reactor.core.publisher.Mono;

public class SigOnRule implements BusinessRule<AuthRequest, String> {
	private UsuarioRepository repository;
	private PasswordEncoder passwordEncoder;
	private JwtUtil jwtUtil;

	public SigOnRule(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Mono<Response<String>> apply(AuthRequest t) {
		var password = passwordEncoder.encode(t.getPassword());
		return repository.criarUsuario(t.getEmail(), password)
				.thenReturn(Response.ok(jwtUtil.generateToken(t.getEmail())));
	}

}
