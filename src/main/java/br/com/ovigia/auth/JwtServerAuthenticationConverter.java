package br.com.ovigia.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {
	private static final String AUTH_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";

	@Override
	public Mono<Authentication> convert(ServerWebExchange exchange) {
		return Mono.justOrEmpty(exchange).map(it -> {

			var headers = exchange.getRequest().getHeaders().get(AUTH_HEADER);
			String token = null;
			if (headers!=null && !headers.isEmpty()) {
				token = headers.get(0);
			}
			if (token != null && token.startsWith(BEARER_PREFIX)) {
				token = token.substring(7);
			}

			return new UsernamePasswordAuthenticationToken(token, token);
		});
	}
}