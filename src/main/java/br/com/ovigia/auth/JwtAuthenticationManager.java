package br.com.ovigia.auth;

import java.util.Arrays;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import reactor.core.publisher.Mono;

public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
	private final JwtSigner jwtSigner;

	public JwtAuthenticationManager(JwtSigner jwtSigner) {
		this.jwtSigner = jwtSigner;
	}

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		return Mono.just(authentication).map(auth -> {
			var credentials = auth.getCredentials();
			var token = credentials != null ? credentials.toString() : null;
			var jws = jwtSigner.parseJwt(token);
			auth = new UsernamePasswordAuthenticationToken(jws.getBody().getSubject(), token,
					Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
			return auth;
		});

	}
}