package br.com.ovigia.auth.businessrule.singon;

import br.com.ovigia.auth.businessrule.SingInResponse;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.repository.UsuarioRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.Date;

public class SingOnRule implements BusinessRule<SignOnRequest, SingInResponse> {
    private UsuarioRepository repository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    public SingOnRule(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Response<SingInResponse>> apply(SignOnRequest request) {
        var mapper = Mappers.getMapper(SignOnRequestMapper.class);
        var usuario = mapper.parse(request);
        usuario.password = passwordEncoder.encode(request.password);
        usuario.dataInicio = new Date();
        return repository.criarUsuario(usuario)
                .thenReturn(Response.ok(new SingInResponse(usuario, jwtUtil.generateToken(request.email))));
    }

}
