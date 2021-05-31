package br.com.ovigia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.repository.VigiaRepository;
import br.com.ovigia.route.ClienteRoutesBuilder;
import br.com.ovigia.route.VigiaRoutesBuilder;
import br.com.ovigia.service.ClienteService;
import br.com.ovigia.service.VigiaService;

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
	public VigiaRepository VigiaRepository() {
		return new VigiaRepository(mongodb());
	}

	@Bean
	public VigiaService vigiaService() {
		return new VigiaService(VigiaRepository());
	}

	@Bean
	public ClienteService clienteService() {
		return new ClienteService();
	}

	@Bean
	public RouterFunction<ServerResponse> rotas() {
		return new VigiaRoutesBuilder(vigiaService()).build().and(new ClienteRoutesBuilder(clienteService()).build());
	}

}