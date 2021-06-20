package br.com.ovigia.auth;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

public class SecurityWebFilter {

	private SecurityWebFilter() {
	}

	public static SecurityWebFilterChain filtering(ServerHttpSecurity http,
			ReactiveAuthenticationManager jwtAuthenticationManager,
			ServerAuthenticationConverter jwtAuthenticationConverter) {
		var authenticationWebFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
		authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter);

		return http.authorizeExchange().pathMatchers("/user/signup").permitAll().pathMatchers("/user/login").permitAll()
				.pathMatchers("/user").authenticated().and()
				.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION).httpBasic().disable()
				.csrf().disable().formLogin().disable().logout().disable().build();
	}
}