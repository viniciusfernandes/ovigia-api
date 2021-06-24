package br.com.ovigia.auth;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

public class SecurityWebFilterBuilder {

	private SecurityWebFilterBuilder() {
	}

	public static SecurityWebFilterChain buildFilter(ServerHttpSecurity http,
			ReactiveAuthenticationManager jwtAuthenticationManager,
			ServerAuthenticationConverter jwtAuthenticationConverter) {
		var authenticationWebFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
		authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter);

		return http.authorizeExchange().pathMatchers("/signup").permitAll().pathMatchers("/login").permitAll()
				.pathMatchers("/user").authenticated().and()
				.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.CORS).httpBasic().disable().csrf()
				.disable().formLogin().disable().logout().disable().build();
	}

}