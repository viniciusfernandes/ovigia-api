package br.com.ovigia.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

	private JwtAuthenticationManager authenticationManager;
	private JwtServerAuthenticationConverter authenticationConverter;

	public WebSecurityConfig(JwtAuthenticationManager authenticationManager,
			JwtServerAuthenticationConverter authenticationConverter) {
		this.authenticationManager = authenticationManager;
		this.authenticationConverter = authenticationConverter;
	}

	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {

		var authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
		authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter);

		return http.exceptionHandling()
				.authenticationEntryPoint(
						(swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
				.accessDeniedHandler(
						(swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
				.and().csrf().disable().formLogin().disable().httpBasic().disable()
				.addFilterAfter(authenticationWebFilter, SecurityWebFiltersOrder.CORS).authorizeExchange()
				.pathMatchers(HttpMethod.OPTIONS).permitAll().pathMatchers("/ovigia/auth/signon").permitAll()
				.pathMatchers("/ovigia/auth/signin").permitAll().anyExchange().authenticated().and().build();
	}

}
