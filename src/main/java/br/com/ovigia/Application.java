package br.com.ovigia;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.auth.repository.UsuarioRepository;
import br.com.ovigia.auth.route.AuthRouter;
import br.com.ovigia.auth.security.CORSFilter;
import br.com.ovigia.auth.security.JwtAuthenticationConverter;
import br.com.ovigia.auth.security.JwtAuthenticationManager;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.auth.security.PBKDF2Encoder;
import br.com.ovigia.auth.security.WebSecurityConfig;
import br.com.ovigia.repository.ClienteRepository;
import br.com.ovigia.repository.RondaRepository;
import br.com.ovigia.repository.VigiaRepository;
import br.com.ovigia.route.ClienteRouter;
import br.com.ovigia.route.RondaRouter;
import br.com.ovigia.route.RoutesBuilder;
import br.com.ovigia.route.VigiaRouter;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.ovigia.error")
public class Application {
	@Autowired
	private GenericApplicationContext context;
	private String secretKey = "ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

	@PostConstruct
	public void registerBeans() {
		registerRepository(context);
		registerSecurityWebFilterChain(context);
		registerCorsFilter(context);
		registerRoutes(context);
	}

	private void registerRepository(GenericApplicationContext context) {
		var mongodb = MongoClients.create().getDatabase("ovigia");
		context.registerBean(MongoDatabase.class, () -> mongodb);

		context.registerBean(VigiaRepository.class, () -> new VigiaRepository(mongodb));
		context.registerBean(ClienteRepository.class, () -> new ClienteRepository(mongodb));
		context.registerBean(RondaRepository.class, () -> new RondaRepository(mongodb));
		context.registerBean(UsuarioRepository.class, () -> new UsuarioRepository(mongodb));
	}

	private void registerSecurityWebFilterChain(GenericApplicationContext context) {
		var jwtUtil = new JwtUtil(secretKey);
		var authManager = new JwtAuthenticationManager(jwtUtil);
		var authConverter = new JwtAuthenticationConverter();

		context.registerBean(JwtUtil.class, () -> jwtUtil);
		context.registerBean(JwtAuthenticationManager.class, () -> authManager);
		context.registerBean(JwtAuthenticationConverter.class, () -> authConverter);
		context.registerBean(PBKDF2Encoder.class, () -> new PBKDF2Encoder());

		ServerHttpSecurity http = context.getBean(ServerHttpSecurity.class);
		context.registerBean(SecurityWebFilterChain.class,
				() -> new WebSecurityConfig(authManager, authConverter).securitygWebFilterChain(http));
	}

	private void registerCorsFilter(GenericApplicationContext context) {
		context.registerBean(CORSFilter.class, () -> new CORSFilter());
	}

	private void registerRoutes(GenericApplicationContext context) {
		var routesBuilder = RoutesBuilder.getInstance();
		routesBuilder.addRouter(new VigiaRouter(getBean(VigiaRepository.class), getBean(ClienteRepository.class)));
		routesBuilder.addRouter(new ClienteRouter(getBean(ClienteRepository.class), getBean(RondaRepository.class)));
		routesBuilder.addRouter(new RondaRouter(getBean(RondaRepository.class)));
		routesBuilder.addRouter(
				new AuthRouter(getBean(UsuarioRepository.class), getBean(PBKDF2Encoder.class), getBean(JwtUtil.class)));

		context.registerBean(RouterFunction.class, () -> routesBuilder.build());
	}

	private <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}

}
