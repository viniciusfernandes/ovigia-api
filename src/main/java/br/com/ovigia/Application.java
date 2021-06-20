package br.com.ovigia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.auth.JwtAuthenticationManager;
import br.com.ovigia.auth.JwtServerAuthenticationConverter;
import br.com.ovigia.auth.JwtSigner;
import br.com.ovigia.auth.SecurityWebFilter;
import br.com.ovigia.businessrule.vigia.CriarVigiaRule;
import br.com.ovigia.repository.ClienteRepository;
import br.com.ovigia.repository.RondaRepository;
import br.com.ovigia.repository.VigiaRepository;
import br.com.ovigia.route.ClienteRouter;
import br.com.ovigia.route.RotaRouter;
import br.com.ovigia.route.RoutesRegister;
import br.com.ovigia.route.VigiaRouter;

@SpringBootConfiguration
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private ServerHttpSecurity serverHttpSecurity;

	@Bean
	public CorsWebFilter corsFilter() {

		CorsConfiguration config = new CorsConfiguration();

		config.setAllowCredentials(true);
		config.addAllowedOrigin("http://localhost:8080");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsWebFilter(source);
	}

	@Bean
	public MongoDatabase mongodb() {
		// return
		// MongoClients.create("mongodb://localhost:27017").getDatabase("ovigia");
		return MongoClients.create().getDatabase("ovigia");
	}

	@Bean
	public VigiaRepository vigiaRepository() {
		return new VigiaRepository(mongodb());
	}

	@Bean
	public ClienteRepository clienteRepository() {
		return new ClienteRepository(mongodb());
	}

	@Bean
	public RondaRepository rotaRepository() {
		return new RondaRepository(mongodb());
	}

	@Bean
	public CriarVigiaRule vigiaService() {
		return new CriarVigiaRule(vigiaRepository());
	}

	@Bean
	public RouterFunction<ServerResponse> rotas() {
		var register = RoutesRegister.getInstance();
		register.registry(new VigiaRouter(vigiaRepository(), clienteRepository()));
		register.registry(new VigiaRouter(vigiaRepository(), clienteRepository()));
		register.registry(new ClienteRouter(clienteRepository(), rotaRepository()));
		register.registry(new RotaRouter(rotaRepository()));
		return register.build();
	}

	@Bean
	public JwtSigner jwtSigner() {
		return new JwtSigner();
	}

	@Bean
	public JwtAuthenticationManager jwtAuthenticationManager() {
		return new JwtAuthenticationManager(jwtSigner());
	}

	@Bean
	public JwtServerAuthenticationConverter jwtServerAuthenticationConverter() {
		return new JwtServerAuthenticationConverter();
	}

	@Bean
	public SecurityWebFilterChain SecurityWebFilter() {
		return SecurityWebFilter.filtering(serverHttpSecurity, jwtAuthenticationManager(),
				jwtServerAuthenticationConverter());
	}

}