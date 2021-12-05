package br.com.ovigia;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
import br.com.ovigia.businessrule.CriarMensalidadesTask;
import br.com.ovigia.businessrule.frequenciaronda.commom.business.CriarFrequenciaRondasBusiness;
import br.com.ovigia.model.repository.ChamadoRepository;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.FaturamentoRepository;
import br.com.ovigia.model.repository.ResumoFrequenciaRondaRepository;
import br.com.ovigia.model.repository.MensalidadeRepository;
import br.com.ovigia.model.repository.ResumoRondaRepository;
import br.com.ovigia.model.repository.RondaRepository;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import br.com.ovigia.model.repository.VigiaRepository;
import br.com.ovigia.route.ChamadoRouter;
import br.com.ovigia.route.ClienteRouter;
import br.com.ovigia.route.ContratoRouter;
import br.com.ovigia.route.MensalidadeRouter;
import br.com.ovigia.route.RondaRouter;
import br.com.ovigia.route.RoutesBuilder;
import br.com.ovigia.route.SolicitacaoVistiaRouter;
import br.com.ovigia.route.VigiaRouter;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.ovigia.error")
public class OvigiaApplication implements CommandLineRunner {
	@Autowired
	private GenericApplicationContext context;
	private String secretKey = "ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength";

	public static void main(String[] args) {
		SpringApplication.run(OvigiaApplication.class, args);

	}

	@PostConstruct
	public void registerBeans() {
		registerRepository(context);
		registerCommomBusiness(context);
		registerSecurityWebFilterChain(context);
		registerCorsFilter(context);
		registerRoutes(context);
	}

	private void registerRepository(GenericApplicationContext context) {
		var mongodb = MongoClients.create().getDatabase("ovigia");
		context.registerBean(MongoDatabase.class, () -> mongodb);

		context.registerBean(SolicitacaoVisitaRepository.class, () -> new SolicitacaoVisitaRepository(mongodb));
		context.registerBean(VigiaRepository.class, () -> new VigiaRepository(mongodb));
		context.registerBean(ClienteRepository.class, () -> new ClienteRepository(mongodb));
		context.registerBean(RondaRepository.class, () -> new RondaRepository(mongodb));
		context.registerBean(ResumoRondaRepository.class, () -> new ResumoRondaRepository(mongodb));
		context.registerBean(UsuarioRepository.class, () -> new UsuarioRepository(mongodb));
		context.registerBean(ChamadoRepository.class, () -> new ChamadoRepository(mongodb));
		context.registerBean(ContratoRepository.class, () -> new ContratoRepository(mongodb));
		context.registerBean(ResumoFrequenciaRondaRepository.class, () -> new ResumoFrequenciaRondaRepository(mongodb));
		context.registerBean(MensalidadeRepository.class, () -> new MensalidadeRepository(mongodb));
		context.registerBean(FaturamentoRepository.class, () -> new FaturamentoRepository(mongodb));
	}

	private void registerCommomBusiness(GenericApplicationContext context) {
		context.registerBean(CriarFrequenciaRondasBusiness.class,
				() -> new CriarFrequenciaRondasBusiness(getBean(ClienteRepository.class),
						getBean(RondaRepository.class), getBean(ResumoFrequenciaRondaRepository.class),
						getBean(VigiaRepository.class)));
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

		routesBuilder.addRouter(
				new ContratoRouter(getBean(ContratoRepository.class), getBean(SolicitacaoVisitaRepository.class),
						getBean(VigiaRepository.class), getBean(ClienteRepository.class)));
		routesBuilder.addRouter(new SolicitacaoVistiaRouter(getBean(SolicitacaoVisitaRepository.class)));
		routesBuilder.addRouter(new ChamadoRouter(getBean(ChamadoRepository.class)));
		routesBuilder.addRouter(new VigiaRouter(getBean(VigiaRepository.class), getBean(ClienteRepository.class)));
		routesBuilder.addRouter(new ClienteRouter(getBean(ClienteRepository.class), getBean(VigiaRepository.class),
				getBean(CriarFrequenciaRondasBusiness.class)));
		routesBuilder.addRouter(new RondaRouter(getBean(RondaRepository.class), getBean(ResumoRondaRepository.class),
				getBean(ChamadoRepository.class), getBean(VigiaRepository.class)));
		routesBuilder.addRouter(
				new AuthRouter(getBean(UsuarioRepository.class), getBean(PBKDF2Encoder.class), getBean(JwtUtil.class)));

		routesBuilder.addRouter(
				new MensalidadeRouter(getBean(MensalidadeRepository.class), getBean(FaturamentoRepository.class)));

		context.registerBean(RouterFunction.class, () -> routesBuilder.build());
	}

	private <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}

	@Override
	public void run(String... args) throws Exception {
		var criarMensalidadesTask = new CriarMensalidadesTask(getBean(ContratoRepository.class),
				getBean(MensalidadeRepository.class));
		criarMensalidadesTask.runTask();
	}

}
