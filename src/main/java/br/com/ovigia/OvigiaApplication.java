package br.com.ovigia;

import br.com.ovigia.auth.route.AuthRouter;
import br.com.ovigia.auth.security.*;
import br.com.ovigia.businessrule.CriarMensalidadesTask;
import br.com.ovigia.businessrule.frequenciaronda.commom.business.CriarFrequenciaRondasBusiness;
import br.com.ovigia.model.repository.*;
import br.com.ovigia.repository.impl.hash.*;
import br.com.ovigia.repository.impl.mongo.*;
import br.com.ovigia.route.*;
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
    private String secretKey = "ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength";
    @Value("${database.impl}")
    private String databaseImpl;

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
        if ("HASH".equals(databaseImpl)) {
            var mongodb = MongoClients.create().getDatabase("ovigia");
            context.registerBean(MongoDatabase.class, () -> mongodb);
            context.registerBean(SolicitacaoVisitaRepository.class, () -> new SolicitacaoVisitaHashRepository());
            context.registerBean(VigiaRepository.class, () -> new VigiaHashRepository());
            context.registerBean(ClienteRepository.class, () -> new ClienteHashRepository());
            context.registerBean(RondaRepository.class, () -> new RondaHashRepository());
            context.registerBean(ResumoRondaRepository.class, () -> new ResumoRondaHashRepository());
            context.registerBean(UsuarioRepository.class, () -> new UsuarioHashRepository());
            context.registerBean(ChamadoRepository.class, () -> new ChamadoHashRepository());
            context.registerBean(ContratoRepository.class, () -> new ContratoHashRepository());
            context.registerBean(MensalidadeRepository.class, () -> new MensalidadeHashRepository());
            context.registerBean(FaturamentoRepository.class, () -> new FaturamentoHashRepository());
        } else if ("MONGO ".equals(databaseImpl)) {
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
        } else {
            throw new IllegalStateException(
                    "Failure on registering the repositories. There is no database implementation with the value: "
                            + databaseImpl);
        }

    }

    private void registerCommomBusiness(GenericApplicationContext context) {
        context.registerBean(CriarFrequenciaRondasBusiness.class,
                () -> new CriarFrequenciaRondasBusiness(getBean(ClienteRepository.class),
                        getBean(RondaRepository.class),
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
