package br.com.ovigia.auth.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
class JwtAuthenticationManager implements ReactiveAuthenticationManager {
	private JwtUtil jwtUtil;

	public JwtAuthenticationManager(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		return Mono.just(authentication).map(auth -> {
			var credentials = auth.getCredentials();

			var token = credentials != null ? credentials.toString() : null;
			if (token == null) {
				return new UsernamePasswordAuthenticationToken(null, null);

			}
			var claims = jwtUtil.getAllClaimsFromToken(token);
			auth = new UsernamePasswordAuthenticationToken(claims, token);
			return auth;
		}).onErrorResume(ex -> {
			return Mono.just(new UsernamePasswordAuthenticationToken(null, null));
		});

	}
}