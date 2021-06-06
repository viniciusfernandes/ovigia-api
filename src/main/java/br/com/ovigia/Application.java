package br.com.ovigia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.businessrule.vigia.CriarVigiaRule;
import br.com.ovigia.repository.ClienteRepository;
import br.com.ovigia.repository.RotaRepository;
import br.com.ovigia.repository.VigiaRepository;
import br.com.ovigia.route.ClienteRoutesBuilder;
import br.com.ovigia.route.RotaRoutesBuilder;
import br.com.ovigia.route.VigiaRoutesBuilder;

@SpringBootConfiguration
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

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
		return MongoClients.create("mongodb://localhost:27017").getDatabase("ovigia");
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
	public RotaRepository rotaRepository() {
		return new RotaRepository(mongodb());
	}

	@Bean
	public CriarVigiaRule vigiaService() {
		return new CriarVigiaRule(vigiaRepository());
	}

	@Bean
	public RouterFunction<ServerResponse> rotas() {
		return new VigiaRoutesBuilder(vigiaRepository(), clienteRepository()).build()
				.and(new ClienteRoutesBuilder(clienteRepository()).build())
				.and(new RotaRoutesBuilder(rotaRepository()).build());
	}

}