package br.com.ovigia.auth.businessrule.singon;

import br.com.ovigia.auth.businessrule.AuthResponse;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.repository.UsuarioRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.Date;

public class SignOnRule implements BusinessRule<SignOnRequest, AuthResponse> {
    private UsuarioRepository repository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private SignOnRequestMapper mapper;

    public SignOnRule(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        mapper = Mappers.getMapper(SignOnRequestMapper.class);
    }

    @Override
    public Mono<Response<AuthResponse>> apply(SignOnRequest request) {
        return Mono.just(mapper.parse(request))
                .onErrorContinue((ex, o) -> Response.badRequest(ex.getMessage()))
                .flatMap(usuario -> {
                    usuario.password = passwordEncoder.encode(request.password);
                    usuario.dataInicio = new Date();
                    return repository.criarUsuario(usuario)
                            .thenReturn(Response.ok(new AuthResponse(usuario, jwtUtil.generateToken(request.email))));
                });
    }

}
