package br.com.ovigia;

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
import br.com.ovigia.model.repository.MensalidadeRepository;
import br.com.ovigia.model.repository.ResumoRondaRepository;
import br.com.ovigia.model.repository.RondaRepository;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import br.com.ovigia.model.repository.UsuarioRepository;
import br.com.ovigia.model.repository.VigiaRepository;
import br.com.ovigia.repository.impl.hash.ChamadoHashRepository;
import br.com.ovigia.repository.impl.hash.ClienteHashRepository;
import br.com.ovigia.repository.impl.hash.ContratoHashRepository;
import br.com.ovigia.repository.impl.hash.FaturamentoHashRepository;
import br.com.ovigia.repository.impl.hash.MensalidadeHashRepository;
import br.com.ovigia.repository.impl.hash.ResumoRondaHashRepository;
import br.com.ovigia.repository.impl.hash.RondaHashRepository;
import br.com.ovigia.repository.impl.hash.SolicitacaoVisitaHashRepository;
import br.com.ovigia.repository.impl.hash.UsuarioHashRepository;
import br.com.ovigia.repository.impl.hash.VigiaHashRepository;
import br.com.ovigia.repository.impl.mongo.ChamadoMongoRepository;
import br.com.ovigia.repository.impl.mongo.ClienteMongoRepository;
import br.com.ovigia.repository.impl.mongo.ContratoMongoRepository;
import br.com.ovigia.repository.impl.mongo.FaturamentoMongoRepository;
import br.com.ovigia.repository.impl.mongo.MensalidadeMongoRepository;
import br.com.ovigia.repository.impl.mongo.ResumoRondaMongoRepository;
import br.com.ovigia.repository.impl.mongo.RondaMongoRepository;
import br.com.ovigia.repository.impl.mongo.SolicitacaoVisitaMongoRepository;
import br.com.ovigia.repository.impl.mongo.UsuarioMongoRepository;
import br.com.ovigia.repository.impl.mongo.VigiaMongoRepository;
import br.com.ovigia.route.ChamadoRouter;
import br.com.ovigia.route.ClienteRouter;
import br.com.ovigia.route.ContratoRouter;
import br.com.ovigia.route.MensalidadeRouter;
import br.com.ovigia.route.RondaRouter;
import br.com.ovigia.route.RoutesBuilder;
import br.com.ovigia.route.SolicitacaoVistiaRouter;
import br.com.ovigia.route.VigiaRouter;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.server.RouterFunction;

import javax.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.ovigia.error")
public class OvigiaApplication implements CommandLineRunner {
    @Autowired
    private GenericApplicationContext context;

    @Value("${database.impl}")
    private String databaseImpl;

    public static void main(String[] args) {
        SpringApplication.run(OvigiaApplication.class, args);
    }

    @PostConstruct
    public void registerBeans() {
        registerRepository(context);
        registerCommonBusiness(context);
        registerSecurityWebFilterChain(context);
        registerCorsFilter(context);
        registerRoutes(context);
        RoutesBuilder.getInstance().logRoutes();
    }

    private void registerRepository(GenericApplicationContext context) {
        if ("HASH".equals(databaseImpl)) {
            registerHashRepository(context);
        } else if ("MONGO".equals(databaseImpl)) {
            registerMongoRepository(context);
        } else {
            throw new IllegalStateException(
                    "Failure on registering the repositories. There is no database implementation with the value: "
                            + databaseImpl);
        }

    }

    private void registerHashRepository(GenericApplicationContext context) {
        context.registerBean(SolicitacaoVisitaRepository.class, SolicitacaoVisitaHashRepository::new);
        context.registerBean(VigiaRepository.class, VigiaHashRepository::new);
        context.registerBean(ClienteRepository.class, ClienteHashRepository::new);
        context.registerBean(RondaRepository.class, RondaHashRepository::new);
        context.registerBean(ResumoRondaRepository.class, ResumoRondaHashRepository::new);
        context.registerBean(UsuarioRepository.class, UsuarioHashRepository::new);
        context.registerBean(ChamadoRepository.class, ChamadoHashRepository::new);
        context.registerBean(ContratoRepository.class, ContratoHashRepository::new);
        context.registerBean(MensalidadeRepository.class, MensalidadeHashRepository::new);
        context.registerBean(FaturamentoRepository.class, FaturamentoHashRepository::new);
    }

    private void registerMongoRepository(GenericApplicationContext context) {
        var mongodb = MongoClients.create().getDatabase("ovigia");
        context.registerBean(MongoDatabase.class, () -> mongodb);
        context.registerBean(SolicitacaoVisitaRepository.class, () -> new SolicitacaoVisitaMongoRepository(mongodb));
        context.registerBean(VigiaRepository.class, () -> new VigiaMongoRepository(mongodb));
        context.registerBean(ClienteRepository.class, () -> new ClienteMongoRepository(mongodb));
        context.registerBean(RondaRepository.class, () -> new RondaMongoRepository(mongodb));
        context.registerBean(ResumoRondaRepository.class, () -> new ResumoRondaMongoRepository(mongodb));
        context.registerBean(UsuarioRepository.class, () -> new UsuarioMongoRepository(mongodb));
        context.registerBean(ChamadoRepository.class, () -> new ChamadoMongoRepository(mongodb));
        context.registerBean(ContratoRepository.class, () -> new ContratoMongoRepository(mongodb));
        context.registerBean(MensalidadeRepository.class, () -> new MensalidadeMongoRepository(mongodb));
        context.registerBean(FaturamentoRepository.class, () -> new FaturamentoMongoRepository(mongodb));
    }

    private void registerCommonBusiness(GenericApplicationContext context) {
        context.registerBean(CriarFrequenciaRondasBusiness.class,
                () -> new CriarFrequenciaRondasBusiness(getBean(ClienteRepository.class),
                        getBean(RondaRepository.class),
                        getBean(VigiaRepository.class)));
    }

    private void registerSecurityWebFilterChain(GenericApplicationContext context) {
        var secretKey = "ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength";
        var jwtUtil = new JwtUtil(secretKey);
        var authManager = new JwtAuthenticationManager(jwtUtil);
        var authConverter = new JwtAuthenticationConverter();

        context.registerBean(JwtUtil.class, () -> jwtUtil);
        context.registerBean(JwtAuthenticationManager.class, () -> authManager);
        context.registerBean(JwtAuthenticationConverter.class, () -> authConverter);
        context.registerBean(PBKDF2Encoder.class, PBKDF2Encoder::new);

        ServerHttpSecurity http = context.getBean(ServerHttpSecurity.class);
        context.registerBean(SecurityWebFilterChain.class,
                () -> new WebSecurityConfig(authManager, authConverter).securitygWebFilterChain(http));
    }

    private void registerCorsFilter(GenericApplicationContext context) {
        context.registerBean(CORSFilter.class, CORSFilter::new);
    }

    private void registerRoutes(GenericApplicationContext context) {
        var routesBuilder = RoutesBuilder.getInstance();

        routesBuilder.addRouter(
                new ContratoRouter(getBean(ContratoRepository.class),
                        getBean(SolicitacaoVisitaRepository.class),
                        getBean(VigiaRepository.class),
                        getBean(ClienteRepository.class),
                        getBean(MensalidadeRepository.class)));
        routesBuilder.addRouter(new SolicitacaoVistiaRouter(getBean(SolicitacaoVisitaRepository.class),
                getBean(VigiaRepository.class)));
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

        context.registerBean(RouterFunction.class, routesBuilder::build);
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
