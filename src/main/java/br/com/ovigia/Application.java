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
import br.com.ovigia.repository.RondaRepository;
import br.com.ovigia.repository.VigiaRepository;
import br.com.ovigia.route.ClienteRouter;
import br.com.ovigia.route.RondaRouter;
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

	@Bean
	public MongoDatabase mongodb() {
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
		register.registry(new RondaRouter(rotaRepository()));
		return register.build();
	}

}