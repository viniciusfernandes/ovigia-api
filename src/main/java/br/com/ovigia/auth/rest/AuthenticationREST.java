package br.com.ovigia.auth.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.ovigia.auth.model.Usuario;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.auth.security.PBKDF2Encoder;
import br.com.ovigia.auth.service.UserService;
import reactor.core.publisher.Mono;

@RestController
public class AuthenticationREST {
	private JwtUtil jwtUtil;
	private PBKDF2Encoder passwordEncoder;
	private UserService userService;

	public AuthenticationREST(JwtUtil jwtUtil, PBKDF2Encoder passwordEncoder, UserService userService) {
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}

	@PostMapping("/ovigia/auth/signin")
	public Mono<ResponseEntity<AuthResponse>> signIn(@RequestBody AuthRequest request) {
		return userService.obterPorEmail(request.getEmail()).filter(password ->

		{
			System.out.println("PAss: " + password);
			return passwordEncoder.encode(request.getPassword()).equals(password);
		}

		).map(userDetails -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails))))
				.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
	}

	@PostMapping("/ovigia/auth/signon")
	public Mono<ResponseEntity<AuthResponse>> signOn(@RequestBody AuthRequest request) {
		var usuario = new Usuario(request.getEmail(), passwordEncoder.encode(request.getPassword()));
		return userService.criarUsuario(usuario)
				.thenReturn(ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(request.getEmail()))));
	}

}
