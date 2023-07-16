package br.com.ovigia.auth.businessrule.singnin;

import br.com.ovigia.auth.businessrule.SingInResponse;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

public class SingInRule implements BusinessRule<SingInRequest, SingInResponse> {
    private UsuarioRepository repository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    public SingInRule(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Response<SingInResponse>> apply(SingInRequest t) {
        var password = passwordEncoder.encode(t.getPassword());
        return repository.obterUsuario(t.getEmail(), password).map(usuario -> {
            var token = jwtUtil.generateToken(t.getEmail());
            return Response.ok(new SingInResponse(usuario, token));
        }).switchIfEmpty(Mono.just(Response.unautorized()));
    }

}
