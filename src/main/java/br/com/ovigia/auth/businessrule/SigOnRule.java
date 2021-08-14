package br.com.ovigia.auth.businessrule;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.ovigia.auth.api.AuthResponse;
import br.com.ovigia.auth.api.SignOnRequest;
import br.com.ovigia.auth.repository.UsuarioRepository;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Usuario;
import reactor.core.publisher.Mono;

public class SigOnRule implements BusinessRule<SignOnRequest, AuthResponse> {
	private UsuarioRepository repository;
	private PasswordEncoder passwordEncoder;
	private JwtUtil jwtUtil;

	public SigOnRule(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Mono<Response<AuthResponse>> apply(SignOnRequest request) {

		var usuario = new Usuario();
		usuario.email = request.email;
		usuario.localizacao = request.localizacao;
		usuario.nome = request.nome;
		usuario.telefone = request.telefone;
		usuario.tipoUsuario = request.tipoUsuario;
		usuario.password = passwordEncoder.encode(request.password);

		return repository.criarUsuario(usuario)
				.thenReturn(Response.ok(new AuthResponse(jwtUtil.generateToken(request.email))));
	}

}
