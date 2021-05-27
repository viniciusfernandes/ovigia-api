package br.com.ovigia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.route.ClienteRoutesBuilder;
import br.com.ovigia.route.VigiaRoutesBuilder;
import br.com.ovigia.service.ClienteService;
import br.com.ovigia.service.VigiaRepository;
import br.com.ovigia.service.VigiaService;

@SpringBootConfiguration
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application implements ApplicationContextInitializer<GenericApplicationContext> {

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

	@Override
	public void initialize(GenericApplicationContext context) {
		MongoRegister.registry(context);
		RepositoryRegister.registry(context);
		ServiceRegister.registry(context);
		RoutesRegister.registry(context);
	}
}

class MongoRegister {

	static void registry(GenericApplicationContext context) {
		MongoDatabase db = MongoClients.create("mongodb://vinicius:teste@localhost:27017").getDatabase("ovigia");
		context.registerBean(MongoDatabase.class, () -> db);
	}
}

class RepositoryRegister {

	static void registry(GenericApplicationContext context) {
		MongoDatabase db = context.getBean(MongoDatabase.class);
		context.registerBean(VigiaRepository.class, () -> new VigiaRepository(db));
	}
}

class ServiceRegister {
	static void registry(GenericApplicationContext context) {
		context.registerBean(VigiaService.class, () -> new VigiaService(context.getBean(VigiaRepository.class)));
	}
}

class RoutesRegister {
	static void registry(GenericApplicationContext context) {
		final var router = new VigiaRoutesBuilder(context.getBean(VigiaService.class)).build()
				.and(new ClienteRoutesBuilder(context.getBean(ClienteService.class)).build());

		context.registerBean(RouterFunction.class, () -> router);
	}
}