package br.com.ovigia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.auth.repository.UsuarioRepository;
import br.com.ovigia.auth.route.AuthRouter;
import br.com.ovigia.auth.security.JwtAuthenticationConverter;
import br.com.ovigia.auth.security.JwtAuthenticationManager;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.auth.security.PBKDF2Encoder;
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
	public UsuarioRepository usuarioRepository() {
		return new UsuarioRepository(mongodb());
	}

	@Bean
	public CriarVigiaRule vigiaService() {
		return new CriarVigiaRule(vigiaRepository());
	}

	@Bean
	public JwtUtil jwtUtil() {
		return new JwtUtil();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new PBKDF2Encoder();
	}

	@Bean
	public JwtAuthenticationManager jwtAuthenticationManager() {
		return new JwtAuthenticationManager(jwtUtil());
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		return new JwtAuthenticationConverter();
	}

	@Bean
	public RouterFunction<ServerResponse> rotas() {
		var register = RoutesRegister.getInstance();
		register.registry(new VigiaRouter(vigiaRepository(), clienteRepository()));
		register.registry(new VigiaRouter(vigiaRepository(), clienteRepository()));
		register.registry(new ClienteRouter(clienteRepository(), rotaRepository()));
		register.registry(new RondaRouter(rotaRepository()));
		register.registry(new AuthRouter(usuarioRepository(), passwordEncoder(), jwtUtil()));
		return register.build();
	}

}