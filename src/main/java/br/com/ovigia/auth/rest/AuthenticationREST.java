package br.com.ovigia.auth.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.ovigia.auth.security.JWTUtil;
import br.com.ovigia.auth.security.PBKDF2Encoder;
import br.com.ovigia.auth.security.model.AuthRequest;
import br.com.ovigia.auth.security.model.AuthResponse;
import br.com.ovigia.auth.service.UserService;
import reactor.core.publisher.Mono;

@RestController
public class AuthenticationREST {
	private JWTUtil jwtUtil;
	private PBKDF2Encoder passwordEncoder;
	private UserService userService;

	public AuthenticationREST(JWTUtil jwtUtil, PBKDF2Encoder passwordEncoder, UserService userService) {
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}

	@PostMapping("/login")
	public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {
		return userService.findByUsername(ar.getUsername())
				.filter(userDetails -> passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword()))
				.map(userDetails -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails))))
				.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
	}

}
