package br.com.ovigia.auth.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

	public AuthenticationManager(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	private JWTUtil jwtUtil;

	@Override
	@SuppressWarnings("unchecked")
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();
		String username = jwtUtil.getUsernameFromToken(authToken);
		return Mono.just(jwtUtil.validateToken(authToken)).filter(valid -> valid).switchIfEmpty(Mono.empty())
				.map(valid -> {
					Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
					List<String> rolesMap = claims.get("role", List.class);
					return new UsernamePasswordAuthenticationToken(username, null,
							rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
				});
	}
}
