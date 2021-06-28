package br.com.ovigia.auth.security;

import java.util.ArrayList;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
class JwtAuthenticationManager implements ReactiveAuthenticationManager {
	public JwtAuthenticationManager(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	private JwtUtil jwtUtil;

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();
		if (jwtUtil.isValidToken(authToken)) {
			String username = jwtUtil.getUsernameFromToken(authToken);
			var authorites = new ArrayList<GrantedAuthority>();
			return Mono.just(new UsernamePasswordAuthenticationToken(username, authToken, authorites));
		}

		return Mono.just(new UsernamePasswordAuthenticationToken(authToken, authToken));
	}
}